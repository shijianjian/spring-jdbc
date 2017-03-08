Material Database
============

The application can be started locally using the following command:

~~~
$ ./gradlew bootRun
~~~

The application can be pushed to cloud foundry using the following command:
~~~
$ ./gradlew assemble
$ cf push
~~~

The application will use the settings in the 'manifest.yml' file.

#### Cloud Foundry Usage

In this example, the application uses a postgres service named 'postgresTest' which could be changed in 'manifest.yml'.

~~~
# view the services available
$ cf marketplace
# create a <service instance>
$ cf create-service <service> <service plan> <service name>
# bind the service instance to the application
$ cf bind-service <app name> <service name>
# restart the application so the new service is detected
$ cf restart
~~~

#### Changing bound services

~~~
$ cf unbind-service <app name> <service name>
$ cf bind-service <app name> <service name>
$ cf restart
~~~
