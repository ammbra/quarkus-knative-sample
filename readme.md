# Serverless with Knative Eventing and Quarkus

The code available here is inspired from https://github.com/piomin/sample-quarkus-serverless-kafka 
and was adapted to use Quarkus native mode and separate databases for each microservice.

In addition to that, `fruit-service` initialization is performed using data from an external service.
A custom health check to this external service was added. 
For the `trade-service` initialization is performed at startup as well.

## Prerequisites

1. Kubernetes cluster with at least 1.17 version. I’m using an OpenShift cluster. 
2. Install Knative Serving and Eventing on your cluster. You may find the detailed installation instructions https://knative.dev/docs/install/any-kubernetes-cluster/[here].
I installed Knative using the Red Hat OpenShift Serverless operator available in OpenShift and instantiated Knative Serving and Eventing
3. Install https://github.com/knative-sandbox/eventing-kafka-broker/releases[Kafka Eventing Broker]. I instantiated it from the operator.
4. Install Kafka cluster either with the Strimzi operator or the Red Hat Integration - AMQ Streams operator available in OpenShift. 
I installed it in the kafka namespace/project. The name of my cluster is my-cluster.
5. Install KafkaSource CRD:
````
kubectl apply -f https://storage.googleapis.com/knative-releases/eventing-contrib/latest/kafka-source.yaml
````

6. For production mode, each of the microservices will have its own database, so please create 3 instances of PostgreSQL.
If you are using OpenShift you can provision those from Developer Catalog. Otherwise, you can provision them locally using Helm Charts:

````
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install postgres \
--set postgresqlUsername=postgres,postgresqlPassword=postgres,postgresqlDatabase=posgres,persistence.enabled=false \
bitnami/postgresql
````

## Deploy each knative service

After you added your own image registry, you can build, push and deploy each Knative service using the command below: 

````
./mvnw package -Pnative -Dquarkus.native.container-build=true  -Dquarkus.container-image.push=true -Dquarkus.native.builder-image=quay.io/quarkus/ubi-quarkus-native-image:21.3-java11
````

If you prefer `kn` CLI:

````
kn service create fruit-service  --image=quay.io/anasandbox/fruit-service:1.0.0-SNAPSHOT --env "KAFKA_TOPIC=trade-events"
kn service create trade-service  --image=quay.io/anasandbox/trade-service:1.0.0-SNAPSHOT --env "KAFKA_TOPIC=trade-events"
kn service create order-service  --image=quay.io/anasandbox/order-service:1.0.0-SNAPSHOT --env "KAFKA_TOPIC=order-events"
````


## Running on OpenShift

1. Login to OpenShift.
2. Create your project: `oc new-project trade`
3. Create Knative `Broker` using `kn broker create trader --namespace knative-eventing` or 
`kubectl apply -f broker.yml`.

4. Create `KafkaBinding` for each application to inject Kafka address

```
 kubectl apply -f fruit-service/src/main/k8s/binding.yml
 kubectl apply -f order-service/src/main/k8s/binding.yml
 kubectl apply -f trade-service/src/main/k8s/binding.yml
```
5. Create `KafkaSource`  for each application to get messages and send them to the appropriate function:
```
 kubectl apply -f fruit-service/src/main/k8s/source.yml
 kubectl apply -f order-service/src/main/k8s/source.yml
 kubectl apply -f trade-service/src/main/k8s/source.yml
```

6. Create `KafkaSource` to get messages from Kafka and send them to the Knative `Broker`

```
 kubectl apply -f source.yaml
```

7. Create Knative `Trigger` for applications:

```
 kubectl apply -f fruit-service/src/main/k8s/trigger.yml
 kubectl apply -f order-service/src/main/k8s/trigger.yml
 kubectl apply -f trade-service/src/main/k8s/trigger.yml
```

8. Create `KafkaSource` for sink:
```
 kubectl apply -f fruit-service/src/main/k8s/source.yml
```

##Autoscaling simulation

1. Delete all the services previously created:

```
kn service delete --all
```

And apply the configuration with autoscaling existing in each application k8s folder:


```
kubectl apply -f fruit-service/src/main/k8s/service.yaml
kubectl apply -f order-service/src/main/k8s/knative.yaml
kubectl apply -f trade-service/src/main/k8s/service.yaml
```
If you issue the following command to get the list of routes you can invoke:


````
kn service list
````

You can use hey to simulate some load and observe autoscaling behavior:

````
hey -z 30s -c 50 <route>  && kubectl get pods --watch
````

