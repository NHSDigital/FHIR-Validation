## AWS 

Use AWS Toolkit terminal 

`aws ecr get-login-password --region eu-west-2 | docker login --username AWS --password-stdin 365027538941.dkr.ecr.eu-west-2.amazonaws.com`

Do this first if app has changed or app code is not present in the static folder
(may need to run `git submodule update` (maybe `git submodule foreach git pull`) and `npm install`)

In **interoperability-standards-tools-skunkworks** folder 

`ng build --configuration production --output-path ../src/main/resources/static --base-href ./`

In root folder 

`mvn clean install -P dockerBuild,dockerRelease,awsRelease`

Run 

`mvn spring-boot:run` and check correct app is working on http://localhost:9001

### Cloud Formation Notes

Do not use

aws cloudformation deploy --template-file C:\Development\NHSDigital\validation-service-fhir-r4\cloudfront\IOPSValidation.yaml --stack-name test-stack
