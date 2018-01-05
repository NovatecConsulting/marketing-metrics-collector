[![License: GPL v3](https://img.shields.io/badge/License-GPL%20v3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0)
[![Build Status](https://travis-ci.org/nt-ca-aqe/marketing-metrics-collector.svg?branch=master)](https://travis-ci.org/nt-ca-aqe/marketing-metrics-collector)
[![codecov](https://codecov.io/gh/nt-ca-aqe/marketing-metrics-collector/branch/master/graph/badge.svg)](https://codecov.io/gh/nt-ca-aqe/marketing-metrics-collector)

Marketing metrics collector is a tool to collect information on different kind of social media modules. It is mainly 
used for collecting information for the IT company NovaTec Consulting GmbH.

### Features

- Collecting metrics from medias..
   - Github
   - Twitter
   - Google Analytics - NovaTec Homepage
   - Google Analytics - NovaTec Blog
   - LinkedIn (currently under development)
   - Xing (currently under development)
   - Facebook (planned)
   - Kununu (planned)
- Saving data to influx database. Saved data can be visualized with tools like Grafana to get a more informative view over 
collected data.
- Custom configuration like collector scheduling, retention of influx data and collector specific configurations like authorization. 
- Every configuration data can be picked up from system variables. Sensitive data like passwords or token can be taken from 
local system variable and passed to a docker image. The classical way of saving data to a property file is also possible.
- Every module represents a microservice that can be run in a docker container. 
- Docker compose can be started by a gradle task. 


### Provider and Consumer

The so called “Social media module”, like Github, Twitter or Google Analytics, is in the role of service Provider. The 
Consumer is the module that fetches information from a media. It consists of collectors, where in most of the cases one 
collector is responsible for one metric. For example, Tweets collector in Twitter Module, collects the number of tweets 
of a user. 

The figure below shows a brief architecture overview for an example module – Twitter in this case. 

![Twitter collector](src/main/resources/images/architecture.jpg)

### Configuration
Probably you don't want to save (and check-in) your security settings like tokens or private keys into the application.yml. 
These settings can be passed by as a system variable to any of the desired microservice. You can set system variables manually when 
you would like to start the collector on your local environment. If you want to use Docker, it is necessary to set desired parameters in the 
docker-compose.yml. To avoid accidentally checking in private tokens, the docker-compose will be ignored by git. The metrics 
collector provides a [docker-compose.yml-example](docker-compose.yml-example) which contains examples for some properties. 
You just need to replace the example file with docuker-compose.yml file. Fill in all parameters you get from your provider (GitHub, 
Twitter, ..), add parameters or remove a service. The name of the system variable is equal to the name of the property in
the application.yml.

Example:

mmc-github/src/main/resources/appllication.yml:
```
github:cron:0 * * * * *
```
docker-compose.yml:
```
services:github:environment:GITHUB_CRON=0 * * * * *
```
Note: The colon in a property-key is an underscore in docker-compose 


To configure the marketing metrics collector, the application.yml in every resources-folder has to be configured. When there
is no value(!) for the key, the system variable, specified in docker-compose will be used automatically.


### Start all services
- start all services, specified in docker-compose:
```
./gradlew composeUp
```

- attach yourself to the logs of all running services:
```
docker-compose logs -f -t
```

- stop all running services:
```
./gradlew composeDown
```

Please make sure to have a running docker-machine when using Docker on MacOS or Windows. For more information read the 
[docker-machine documenation](https://docs.docker.com/machine/).

### Issues
If you experience any issues please use GitHub's [issue](https://github.com/nt-ca-aqe/marketing-metrics-collector/issues) 
system to tell us about it!

### Licensing
Metrics Marketing Collector is licensed under [GNU General Public License 3](http://www.gnu.org/licenses/gpl-3.0).

### Sponsoring
Metrics Marketing Collector is mainly developed by [NovaTec Consulting GmbH](http://www.novatec-gmbh.de/), a German 
consultancy firm that drives quality in software development projects.