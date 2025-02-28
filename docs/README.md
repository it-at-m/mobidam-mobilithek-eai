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

```yaml
de.muenchen.mobidam:
  integration:
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

```yaml
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

Jede Mobilithek Schnittstelle hat eine individuelle _mobilithek-url_.

```yaml
de.muenchen.mobidam:
  integration:
    ...
    interfaces:
      interface-1:
        ...
        mobilithek-url: ...
        ...
```

Für den Zugriff auf die Mobilithek stellt diese ein individuelles Zertifikat _certificate.p12_ zur Verfügung. 
Das Zertifikat muss zusammen mit den anderen LHM Zertifikaten, z.Bsp. für den Zugriff auf Mobidam-Sst-Management, in der cacerts Datei des CAP Containers verfügbar sein. 
In der CAP wird dazu in der Pipeline eine entsprechende Datei erstellt deren Ablageort und ihr Passwort in den nachfolgenden Attributen angegeben werden muss.

Kubernetes/Openshift Secret:
```yaml
de.muenchen.mobidam:
  ..
  eai:
    cacerts-file: ...
    cacerts-password: ...
     
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



```yaml
de.muenchen.mobidam:
  integration:
    ...
    interfaces:
      interface-1:
        ...
        s3-object-path: ...
        s3-date-format: ...
        s3-bucket: s3-bucket-1
  common:
    s3:
      bucket-credential-config:
        s3-bucket-1:
          access-key-env-var: ...
          secret-key-env-var: ...
```




### Cronjob
Last but not least muss der Cronjob pro Schnittstelle auch noch ausgelöst werden:

```yaml
de.muenchen.mobidam:
  integration:
    ...
    interfaces:
      interface-1:
        ...
        cron-expression: ...
        ...
```



### Schadcode-Erkennung
Mit der Konfiguration _malicious-code-detection-enabled_ läßt sich die Schadcode-Erkennung einschalten:
```yaml
de.muenchen.mobidam:
  integration:
    interfaces:
      parkRideStaticData:
        allowed-resource-types: xml
        malicious-code-detection-enabled: true
  ...
  data:
    defined-resource-types:
      resource-types:
        xml:
          allowed-mime-types: application/xml, text/plain
        plain:
          allowed-mime-types: text/plain
        csv:
          allowed-mime-types: text/csv
```
#### Begriffsklärung 'Resource-Typ'
Der Begriff _**Resource-Typ**_ wird benutzt um sich von den Begriffen _MimeType_ und _ContentType_ abzugrenzen und keine 
Irritationen bei den Inhalten der Resource-Typen Bezeichner entstehen. 

* _Mime-Type / Media-Type_:  Mime-Type kommt ursprünglich aus der EMail Welt um von ASCII abweichende Dateninhalte zu unterstützen.
Der Mime-Type wird in RFCs auch gerne durch die Bezeichnung Media-Type ersetzt. Er hat ein Multipart Format : type/subtype z. Bsp. text/plain, image/jpg.
* _Content-Type_: Content-Type ist ein HTTP-Header. Er gibt den Media-Type an und kann eine Erweiterung enthalten. Er hat ein Multipart Format mit optionalen Parameter : type/subtype; parameter=value z.Bsp. text/html; charset=UTF-8 oder auch nur application/json.
Ist kein optionaler Charset Parameter angegeben ist von einem Standard UTF-8 Charset auszugehen.

Die Resource-Typen werden verwendet das von den Datenquellen erhaltenen Datenformat zu Prüfen und auf Schadcode zu untersuchen. 
Die Inhalte _interfaces.[interface].**allowed-resource-types**_ und _data.defined-resource-types.resource-types.**[resource-type]**_ 
sind frei wählbar während die Inhalte in _data.defined-resource-types.resource-types.[resource-type].**allowed-mime-types**_ sich 
an die gängigen Konventionen halten müssen.
In ihrer Kombination lassen sich mit den _type/subtype_-Kombinationen eigene Schadcode-Parser konfigurieren.

#### Resource-Typ Prüfung
Mit der Kombination aus _interfaces.[interface].**allowed-resource-types**_ und _data.defined-resource-types.resource-types.[resource-type].**allowed-mime-types**_ werden die für die Schnittstelle zugelassenen _Resource-Types_ konfiguriert. 
Dazu wird in jeder Schnittstelle mindestens ein erlaubter _type_ als _allowed-resource-types_ spezifiziert. Der _**allowed-resource-types**_ braucht eine Entsprechung in _data.defined-resource-types.resource-types.[**resource-type**]_, der wiederum alle erlaubten _type/subtype_ spezifiziert. 
Im Bsp. findet der Ressource-Typ _interfaces.[interface].allowed-resource-types: **xml**_ die definierten Mime-Types _data.defined-resource-types.resource-types.**xml**: application/xml, text/plain_.

Der Resource-Type Check durchläuft zwei Prüfungen. 
- Prüfung ob der von der Datenquelle gelieferte [HTTP ContentType](https://developer.mozilla.org/de/docs/Web/HTTP/Headers/Content-Type) als _defined-resource-type_ gelistet ist.
- Mit [Tika](https://tika.apache.org/) wird der Inhalt der Datei auf seinen Content-Type untersucht und geprüft ob der von Tika ermittelte Type als _defined-resource-type_ gelistet ist. 

Es lassen sich auch mehrere _allowed-resource-types_ spezifizieren. 
- Die Resources von _interfaces.[interface].allowed-resource-types_ werden dann in der Reihenfolge der _data.defined-resource-types.resource-types.[...]_ konkateniert. Im Beispiel unten sind die _allowed-resource-types: csv, plain_ in der Reihenfolge _text/csv, text/plain_ erlaubt. 
- Doppelte Resource-Types werden entfernt. Beispielsweise werden aus der Kombination _allowed-resource-types: plain, xml_ die _allowed-mime-types: text/plain, application/xml_.

```yaml
de.muenchen.mobidam:
  integration:
    interfaces:
      parkRideStaticData:
        allowed-data-types: 
          - csv
          - plain
        malicious-code-detection-enabled: true
  ...
  data:
    defined-resource-types:
      resource-types:
        xml:
          allowed-mime-types: application/xml, text/plain
        plain:
          allowed-mime-types: text/plain
        csv:
          allowed-mime-types: text/csv
```

#### Schadcode Prüfung
Durch die Kombination aus _allowed-resource-types_ und _data.defined-resource-types.resource-types.[resource-type].allowed-mime-types_ lässt sich die Schadcode Analyse konfigurieren.

Der erste konfigurierte _allowed-mime-types_ im Format _type/subtype_ wird dazu verwendet, einen geeigneten Dateiparser bereitzustellen.
Aktuell stehen ein XML (_application/xml_), CSV (_text/csv_) und Default Dateiparser zur Verfügung, die den Dateiinhalt auf Schadcode wie z.Bsp. unerlaubte Binärzeichen, XSS-Code, etc. durchsucht.

Soll mit der Beispielkonfiguration eine XML Datei mit dem XML-Parser auf Schadcode untersucht werden muss der _allowed-resource-types: xml_ als erstes stehen. In der Beispiel Konfiguration würde auch eine XML Datei mit dem [HTTP ContentType](https://developer.mozilla.org/de/docs/Web/HTTP/Headers/Content-Type) text/plain mit dem XML-Parser untersucht werden.

_review-specification.malicious-data-regex_ : Für die Analyse von CSV Dateien können beliebige viele Regex-Ausdrücke unter einem eindeutigen beschreibenden Bezeichner wie z.Bsp. _excel, script, sql etc._ definiert werden.
Alle Regex-Ausdrücke werden auf jeden Zellinhalt der CSV Datei angewendet. 

```yaml
de.muenchen.mobidam:
  ...
  data:
    review-specification:
      malicious-data-regex:
        excel: [Regex Ausdruck]
        script: [Regex Ausdruck]
        sql: [Regex Ausdruck]
  ...
```

### Beispielkonfiguration:

```yaml
de.muenchen.mobidam:
  integration:
    baseUrl: https://mobidam-sst-management...
    interfaces:
      parkRideStaticData:
        mobidam-sst-id: 999fcf2d-25bb-4fa9-85ff-f7ed12349999
        name: P+R Statisch
        mobilithek-url: https://mobilithek.info:8443/mobilithek/api/v1.0/subscription/123456789/clientPullService?subscriptionID=123456789
        cron-expression: '0 * * ? * *'
        s3-object-path: MDAS/Mobilithek/PR-static/%s-pr-daten.xml
        s3-date-format: yyyyMMdd_HHmmss
        s3-bucket: my-bucket-name
        allowed-resource-types: 
          - xml
          - plain
        malicious-code-detection-enabled: true
      parkRideDynamicData:
        mobidam-sst-id: 888fcf2d-25bb-4fa9-85ff-f7ed12348888
        name: P+R Dynamisch
        mobilithek-url: https://mobilithek.info:8443/mobilithek/api/v1.0/subscription/1234567891/clientPullService?subscriptionID=1234567891
        cron-expression: '30 * * ? * *'
        s3-object-path: MDAS/Mobilithek/PR-dynamic/%s-pr-daten.xml
        s3-date-format: yyyyMMdd_HHmmss
        s3-bucket: my-bucket-name  
        allowed-resource-types: 
          - xml
          - plain
        malicious-code-detection-enabled: true
    data:
      defined-resource-types:
        resource-types:
          xml:
            allowed-mime-types: application/xml, text/plain
          plain:
            allowed-mime-types: text/plain
          csv:
            allowed-mime-types: text/csv
      review-specification:
        malicious-data-regex:
          excel: ^[=]\w*
          script: .*\.(exe)
          sql: drop\s.*
  eai:
    cacerts-file: 'file:/mnt/cacerts'
    cacerts-password: my-password
  common:
    s3:
      bucket-credential-config:
        my-bucket-name:
          access-key-env-var: MY_ACCESS_KEY
          secret-key-env-var: MY_SECRET_KEY
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

```
## Grafana
Die Mobilithek Grafana Dashboards für die Umgebungen _mobidam-dev_ und _mobidam_ sind  über den 'CAP Grafana Overview' der jeweiligen Umgebungen erreichbar. Die URLs der 'Overviews' finden sich im LHM-CAP-Wiki.
Bislang sind nur für Umgebungen _mobidam-dev_ und _mobidam_ Grafana-Operatoren eingerichtet und Dashboards verfügbar:

- _<mobidam-grafana-url>/Dashboards/<kubernetes-namespace>/mobidam-mobilithek-eai-<environment>-cap_

Von der mobidam-mobilitheks-eai werden verschiedene auf Probleme hinweisende Metriken für die Dashboards zur Auswertung zur Verfügung gestellt:
- Kennwerte die Verarbeitung von Camel Exchanges betreffen.
- Kennwerte die auf Probleme bei einzelnen Schnittstellen hinweisen.
- Kennwerte die die Verarbeitung von Downloads erfassen.

Jede Dashboard Visualisierung stellt weiter Erklärungen zu ihren Inhalten zur Verfügung.

Das Dashboard ist gemäß der Anleitung im LHM-CAP-Wiki als Kubernetes Dashboard Manifest gesichert. 
Alle Änderungen die im Grafana Dashboard gemacht werden, müssen im Kubernetes Manifest widergespiegelt werden. 
Dazu das _Grafana -> Settings -> JSON Model_ im Kubernetes Dashboard Manifest per Copy-Paste aktualisieren. 
Anschließend werden durch Kubernetes alle Mainifest Änderungen wieder mit dem Grafana Dashboard aktualisiert und alle Umgebungen sind wieder up-to-date.

Um Dashboard Aktualisierungen von K auf P zu bringen, die _JSON Model_ Änderungen in den Kubernetes Dashboards Manifeste der Namespaces synchronisieren.
Die Grafana Instanzen auf K und P sind eigenständig. Daher kann die Grafana Dashboard-UID beibehalten werden.
Bei Problemen mit der Dashboard-UID diese einfach neu Vergeben. 
Ist bei der Aktualisierung des Grafana Dashboards aus dem Kubenetes Manifest heraus keine Dashboard-UID vorhanden, wird diese automatisch neu generiert. 
Die UID kann im Zweifel im Kubernetes Dashboard Manifest also gelöscht werden.

Alle von der mobidam-mobilithek-eai zur Verfügung gestellten Metriken können im Pod-Terminal mit _curl localhost:8080/actuator/prometheus_ angezeigt werden.
Das ist zum Beispiel hilfreich um eine Überblick über alle Metrik Identifier mit ihren Werten zum Abfragezeitunkt zu bekommen.
Einige Schnittstelle spezifische Metriken werden dynamisch erzeugt und sind erst sichtbar wenn die Schnittstelle aktiviert und erfolgreich beendet wurde.
Wenn im Dashboard dazu trotzdem Werte angezeigt werden die in der der curl-Abfrage nicht sichtbar sind, liegt das daran das die Dashboard Übersichten Pod übergreifend ihre Werte ermitteln können.
So sind auch Abweichungen bei den Werten zwischen Pod-Abfrage und Grafana Dashboard zu erklären


