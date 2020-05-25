
# Helidon Tickets App

This project implements a simple REST service using Helidon SE to display a list of tickets.

## Prerequisites

1. Maven 3.5 or newer
2. Java SE 8 or newer
3. Docker 17 or newer to build and run docker images
4. Kubernetes minikube v0.24 or newer to deploy to Kubernetes (or access to a K8s 1.7.4 or newer cluster)
5. Kubectl 1.7.4 or newer to deploy to Kubernetes

Verify prerequisites
```
java -version
mvn --version
docker --version
minikube version
kubectl version --short
```

## Build

```
mvn package
```

## Start the application

```
java -jar target/helidon-tickets-app.jar
```

## Exercise the CRUD application

```
curl -X GET http://localhost:8080/tickets
- A list of tickets

curl -X GET http://localhost:8080/tickets/status/Resolved
- get tickets by status

curl -X GET http://localhost:8080/tickets/product/Lenovo
- get tickets by product

curl -X GET http://localhost:8080/tickets/customer/Smithers
- get tickets by customer

curl -X GET http://localhost:8080/tickets/creationDate/05-04-2020
- get tickets by creationDate

curl -X GET http://localhost:8080/tickets/45434
- get ticket by id


curl -X PUT -H "Content-Type: application/json" -d '{ "id": "45434", "creationDate": "05-25-2020", "customer": "Tech Experts Up", "customerId": "46995", "partner": "We-fix-em Computer Services", "partnerId": "7309", "product": "Lenovo Laptop Computer", "status": "Unresolved", "subject": "Keys are sticking", "summary": "Customer reports that keys are sticking after spilling a sugar-laden soft-drink on it" }' http://localhost:8080/tickets/45434
- update ticket with new contents, don't forget to send all of the request parameters, otherwise it just hangs out



 curl -X POST -H "Content-Type: application/json" -d '{ "id": "", "creationDate": "05-25-2020", "customer": "iShop Q", "customerId": "47886", "partner": "We-fix-em Computer Services", "partnerId": "7309", "product": "Docking Station Lenovo Yoga", "status": "Unresolved", "subject": "USB ports not recognizing devices", "summary": "Customer reports that none of the USB ports are working after connecting devices" }' http://localhost:8080/tickets
-Add new incident


curl -X DELETE http://localhost:8080/tickets/45434
- Delete ticket by id

```

## Try health and metrics

```
curl -s -X GET http://localhost:8080/health
{"outcome":"UP",...
. . .

# Prometheus Format
curl -s -X GET http://localhost:8080/metrics
# TYPE base:gc_g1_young_generation_count gauge
. . .

# JSON Format
curl -H 'Accept: application/json' -X GET http://localhost:8080/metrics
{"base":...
. . .

```

## Build the Docker Image

```
docker build -t helidon-tickets-app .
```

## Start the application with Docker

```
docker run --rm -p 8080:8080 helidon-tickets-app:latest
```

Exercise the application as described above

## Deploy the application to Kubernetes

```
kubectl cluster-info                # Verify which cluster
kubectl get pods                    # Verify connectivity to cluster
kubectl create -f app.yaml   # Deply application
kubectl get service employee-app  # Get service info
```

## Native image with GraalVM

GraalVM allows you to compile your programs ahead-of-time into a native
 executable. See https://www.graalvm.org/docs/reference-manual/aot-compilation/
 for more information.

You can build a native executable in 2 different ways:
* With a local installation of GraalVM
* Using Docker

### Local build

Download Graal VM at https://github.com/oracle/graal/releases, the version
 currently supported for Helidon is `19.0.0`.

```
# Setup the environment
export GRAALVM_HOME=/path
# build the native executable
mvn package -Pnative-image
```

You can also put the Graal VM `bin` directory in your PATH, or pass
 `-DgraalVMHome=/path` to the Maven command.

See https://github.com/oracle/helidon-build-tools/tree/master/helidon-maven-plugin
 for more information.

Start the application:

```
./target/helidon-tickets-app
```

### Multi-stage Docker build

Build the "native" Docker Image

```
docker build -t helidon-tickets-app -f Dockerfile.native .
```

Start the application:

```
docker run --rm -p 8080:8080 helidon-tickets-app:latest
```