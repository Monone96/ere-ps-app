package health.ere.ps.service.connector.certificate;

import de.gematik.ws.conn.cardservice.wsdl.v8.CardServicePortType;
import de.gematik.ws.conn.cardservicecommon.v2.PinResultEnum;
import de.gematik.ws.conn.certificateservice.v6.ReadCardCertificate;
import de.gematik.ws.conn.certificateservice.v6.ReadCardCertificateResponse;
import de.gematik.ws.conn.certificateservice.wsdl.v6.CertificateServicePortType;
import de.gematik.ws.conn.certificateservice.wsdl.v6.FaultMessage;
import de.gematik.ws.conn.certificateservicecommon.v2.CertRefEnum;
import de.gematik.ws.conn.certificateservicecommon.v2.X509DataInfoListType;
import de.gematik.ws.conn.connectorcommon.v5.Status;
import de.gematik.ws.conn.connectorcontext.v2.ContextType;
import health.ere.ps.exception.connector.ConnectorCardCertificateReadException;
import health.ere.ps.service.connector.provider.ConnectorServicesProvider;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.xml.ws.Holder;
import java.math.BigInteger;
import java.util.logging.Logger;

@ApplicationScoped
public class CardCertReadExecutionService {
    private static final Logger log = Logger.getLogger(CardCertReadExecutionService.class.getName());

    static {
        System.setProperty("javax.xml.accessExternalDTD", "all");
    }

    @Inject
    ConnectorServicesProvider connectorServicesProvider;


    /**
     * Reads the AUT certificate of a card.
     *
     * @param cardHandle The handle of the card whose AUT certificate is to be read.
     * @return The read AUT certificate.
     */
    public ReadCardCertificateResponse doReadCardCertificate(String cardHandle)
            throws ConnectorCardCertificateReadException {

        ReadCardCertificate.CertRefList certRefList = new ReadCardCertificate.CertRefList();
        certRefList.getCertRef().add(CertRefEnum.C_AUT);

        Holder<Status> statusHolder = new Holder<>();
        Holder<X509DataInfoListType> certHolder = new Holder<>();

        try {
            connectorServicesProvider.getCertificateService().readCardCertificate(cardHandle, connectorServicesProvider.getContextType(), certRefList,
                    statusHolder, certHolder);
        } catch (FaultMessage faultMessage) {
            // Zugriffsbedingungen nicht erfüllt
            boolean code4085 = faultMessage.getFaultInfo().getTrace().stream()
                    .anyMatch(t -> t.getCode().equals(BigInteger.valueOf(4085L)));

            if (code4085) {
                Holder<Status> status = new Holder<>();
                Holder<PinResultEnum> pinResultEnum = new Holder<>();
                Holder<BigInteger> error = new Holder<>();
                try {
                    connectorServicesProvider.getCardServicePortType().verifyPin(connectorServicesProvider.getContextType(), cardHandle, "PIN.SMC", status, pinResultEnum, error);
                    doReadCardCertificate(cardHandle);
                } catch (de.gematik.ws.conn.cardservice.wsdl.v8.FaultMessage e) {
                    throw new ConnectorCardCertificateReadException("Could not get certificate", faultMessage);
                }
            } else {
                throw new ConnectorCardCertificateReadException("Could not get certificate", faultMessage);
            }
        }

        ReadCardCertificateResponse readCardCertificateResponse = new ReadCardCertificateResponse();

        readCardCertificateResponse.setStatus(statusHolder.value);
        readCardCertificateResponse.setX509DataInfoList(certHolder.value);

        return readCardCertificateResponse;
    }
}
