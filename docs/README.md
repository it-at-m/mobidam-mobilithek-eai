# Dokumentation

- Die Enterprise Application Integration Komponente (EAI) kopiert Dateien aus einem angegebenen Konto der [Mobilithek.Info](https://mobilithek.info/) in einen S3 Bucket.

- Die Zugriffsaktivtäten werden im [Mobidam-Sst-Management](https://github.com/it-at-m/mobidam-sst-management) per logging dokumentiert. Dafür stellt das [Mobidam-Sst-Management](https://github.com/it-at-m/mobidam-sst-management) eine [Integrationskomponente](https://github.com/it-at-m/mobidam-sst-management/tree/sprint/mobidam-sst-management-integration) zur Verfügung.

- Pro Mobilithek Schnittstelle wird ein Cronjob mit einem Downloadintervall konfiguriert.

Github-Repo:  https://github.com/it-at-m/mobidam-mobilithek-eai

## Technisches Setup
- Für jede herunter zu ladende Datei aus einem Mobilithek Konto müssen deren Zugangskontodaten angegeben werden.
- Für den Zugriff auf mobidam-sst-management müssen die Schnittstellen und die Authentifizierung bekannt sein.
- Für die Ablage im S3 müssen alle erforderlichen Zugangsdaten zum S3 Bucket verfügbar sein.

### [Mobidam-Sst-Management](https://github.com/it-at-m/mobidam-sst-management)
Alle Mobilithek Schnittstellen werden in der Mobidam-SST-Management Komponente verwaltet und müssen dort angelegt bzw. registriert sein.
Über das Mobidam-SST-Management können Mobilithek Schnittstellen aktiviert und deaktiviert werden. Alle relevanten Logging Informationen zu den Schnittstellen werden im Mobidam-SST-Management zentral gesammelt.

Die _baseUrl_ ist die allgemeine Mobidam-SST-Management URL.

_interfaces_ ist eine Schnittstellen Liste, d.h. es können mehrere Mobilithek Info Schnittellen konfiguriert werden.

Der Bezeichner _interfaces-1_ ist frei wählbar und dient der Identifizierung der Mobilithek Schnittstelle. Siehe unten die Beispielkonfiguration.

Um aus der mobidam-mobilithek-eai eine Schnittstelle im Mobidam-SST-Management ansprechen zu können muss deren individuelle _mobidam-sst-id_ bekannt sein.

```
de.muenchen.mobidam.integration:
  baseUrl: ...
  interfaces:
    interface-1:
      mobidam-sst-id: ...
      ...
   interface-2:
      mobidam-sst-id: ... 
      ...  
```

Für den erfolgreichen Mobidam-SST-Management Zugriff muss eine valide OAuth2 Authenthifizierung eingerichtet sein.
Die Angaben sind für eine erfolgreiche Initialisierung der [Integrationskomponente](https://github.com/it-at-m/mobidam-sst-management/tree/sprint/mobidam-sst-management-integration) erforderlich.

```
spring:
  ...
  security:
    oauth2:
      client:
        registration:
          custom:
            provider: ...
            client-id: ...
            client-secret: ...
            authorization-grant-type: ...
        provider:
          custom:
            token-uri: ..
            
```

### [Mobilithek.Info](https://mobilithek.info/)

Jede Mobilithek Schnittstelle hat eine individuelle _mobilithek-subscription-id_ und _mobilithek-url_.

```
de.muenchen.mobidam.integration:
  ...
  interfaces:
    interface-1:
      ...
      mobilithek-subscription-id: ...
      mobilithek-url: ...
      ...
```

Für den Zugriff auf die Mobilithek stellt diese ein individuelles Zertifikat _certificate.p12_ inkl. Password zur Verfügung. 
Das Zertifikat muss in einer Datei Namens _mobilithek.jks_ im Classpath der mobilithek-info-eai liegen.
Die _mobilithek.jks_ kann mit folgendem Befehl erzeugt werden. Dabei muss das Mobilithek Password für 
'certificate.p12' und das Password für den zu erzeugenden Keystore 'mobilithek.jks' von Hand eingegeben werden: 
```
keytool -importkeystore -srckeystore [certificate.p12] -srcstoretype PKCS12 -keystore mobilithek.jks
```

Das von Hand eingegebene Keystore Password muss als  _jks_password_ in einem Secret in der Openshift Konfiguration angegeben werden.

Kubernetes/Openshift Secret:
```
mobidam:
  mobilithek:
    jks-password: changeit
     
```


### S3 Bucket

Pro Mobilithek Schnittstelle muss die Ablage im S3 Bucket _s3-bucket_ definiert werden.

Die Download Datei aus der Mobilithek wird unter einen individuellen Namen inkl. Zeitstempel im angebenen S3 Bucket angelegt.
Dazu dienen _s3-object-path_ und _s3-date-format_. Z.Bsp. wird mit den Angaben 

- s3-object-path: _MDAS/Mobilithek/PR-statisch/%s-pr-daten.xml_
- s3-date-format: _yyyyMMdd_HHmmss_

folgender S3 Bucket Path/Dateiname generiert, wobei das _s3-date-format_ an der Possition %s eingefügt wird: 

- _MDAS/Mobilithek/PR-statisch/20240513_165114-pr-daten.xml_

Der _s3-bucket_ Name aus der Mobidam Schnittstelle muss zu den Angaben in der _bucket-credential-config_ Liste passen, sonst können der S3 _access-key-env-var_ und _secret-key-env-var_ nicht korrekt ermittelt werden. Sie auch die Beschreibung [mobidam-s3-eai](https://github.com/it-at-m/mobidam-s3-eai/blob/sprint/docs/README.md#konfiguration).



```

de.muenchen.mobidam.integration:
  ...
  interfaces:
    interface-1:
      ...
      s3-object-path: ...
      s3-date-format: ...
      s3-bucket: s3-bucket-1

mobidam:
  s3:
    bucket-credential-config:
      s3-bucket-1:
        access-key-env-var: ...
        secret-key-env-var: ...

```




### Cronjob
Last but not least muss der Cronjob pro Schnittstelle auch noch ausgelöst werden:

```
de.muenchen.mobidam.integration:
  ...
  interfaces:
    interface-1:
      ...
      cron-expression: ...
      ...
```



### Beispielkonfiguration:

```
de.muenchen.mobidam.integration:
  baseUrl: https://mobidam-sst-management...
  interfaces:
    parkRideData:
      mobidam-sst-id: 999fcf2d-25bb-4fa9-85ff-f7ed12349999
      mobilithek-subscription-id: 123456789
      mobilithek-url: https://mobilithek.info:8443/mobilithek/api/v1.0/subscription/123456789/clientPullService?subscriptionID=123456789
      cron-expression: '0 * * ? * *'
      s3-object-path: MDAS/Mobilithek/PR-statisch/%s-pr-daten.xml
      s3-date-format: yyyyMMdd_HHmmss
      s3-bucket: my-bucket-name

spring:
  security:
    oauth2:
      client:
        registration:
          custom:
            provider: custom
            client-id: my-client-id
            client-secret: my-client-secret
            authorization-grant-type: client_credentials
        provider:
          custom:
            token-uri: https://.../realms/[my-realm]]/protocol/openid-connect/token

mobidam:
  s3:
    bucket-credential-config:
      my-bucket-name:
        access-key-env-var: MY_ACCESS_KEY
        secret-key-env-var: MY_SECRET_KEY

```
