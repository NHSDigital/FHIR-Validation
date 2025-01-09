# FHIR Development and Testing (FHIR Validation) Skunkworks

This project is classed as **skunkworks** it is not built for operational use.

See [FHIR Development and Testing (FHIR Validation) Skunkworks](http://lb-fhir-validator-924628614.eu-west-2.elb.amazonaws.com/swagger-ui/index.html) for a demonstration of this service can be found on 

An application using this service: [FHIR Development and Testing Tools (skunkworks)](https://nhsdigital.github.io/interoperability-standards-tools-skunkworks/)

This project has three purposes: 

1. To provide a FHIR Validation Service which runs via **AWS ECS** and provides a FHIR /$validate operation. See [deploy-notes](deploy-notes.md)
2. To provide a FHIR Validation Service which runs via **Docker** and provides a FHIR /$validate operation. See [docker-image](docker-image.md)
3. To provide a FHIR Validation service for **GitHub Actions** to test FHIR Conformance. See [IOPS-FHIR-Test-Scripts](https://github.com/NHSDigital/IOPS-FHIR-Test-Scripts) for example github actions using this service.

This service is built using [HL7 Validation and HAPI FHIR Libaries](https://hapifhir.io/hapi-fhir/docs/validation/introduction.html) with additional support for 

- [NHS England Terminology Server](https://digital.nhs.uk/services/terminology-server) to handle the security layer.
- [FHIR Message](https://hl7.org/fhir/R4/messaging.html) validation using a FHIR MessageDefinition.

## Related Projects

- [validation-service-fhir-r4](https://github.com/NHSDigital/validation-service-fhir-r4) is used to perform operational FHIR Validation Service by [Electronic Prescription Service - FHIR API](https://digital.nhs.uk/developer/api-catalogue/electronic-prescription-service-fhir). This provides FHIR Validation via a **AWS Lambda** and is optimised for performance (it doesn't perform coding validation).  

## Configuration

It has several configuration options: 

- To validate against a supplied set of FHIR Implementation Guides (NPM packages are supplied).
- To validate against a configured FHIR Implementation Guide (NPM package are retrieved by the service and configured via environment variables)
- Optionally validate using the NHS England Ontology Service (configured via environment variables).

The configuration is aimed at supporting different use cases. For example the lambda version with no ontology support is aimed at performing basic FHIR validation checks. This may just be FHIR core and schema validation but can also test against UKCore profiles.

See [Environmental Variables](environment-variables.md) for configuration options.

## Architecture

The Validator is based on [HAPI FHIR Instance Validator](https://hapifhir.io/hapi-fhir/docs/validation/instance_validator.html), which is exposed using a [HAPI FHIR Plain Server](https://hapifhir.io/hapi-fhir/docs/server_plain/server_types.html) which gives an FHIR RESTful API to a [FHIR $validate operation](https://www.hl7.org/fhir/resource-operation-validate.html). 
This API is documented via [OpenAPI Specification (swagger)](https://swagger.io/specification/) as part of the service [here](http://lb-fhir-validator-924628614.eu-west-2.elb.amazonaws.com/swagger-ui/index.html)  

![validator component diagram](./input/images/component-diagram.png)

Internally validation configuration is composed of: 

- `DefaultProfileValidationSupport` - which includes base FHIR CodeSystems and ValueSets
- `CommonCodeSystemsTerminologyService` - which includes UK Core and NHS England FHIR NPM packages
- A customised version of `TerminologyServiceValidationSupport` to handle the security mechanism of NHS England's Terminology Server
- `AWSValidationSupport` a custom class to enable validation of FHIR QuestionnaireResponse using FHIR Questionnaire's stored a AWS FHIRWorks server.
