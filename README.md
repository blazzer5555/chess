# ♕ BYU CS 240 Chess

This project demonstrates mastery of proper software design, client/server architecture, networking using HTTP and WebSocket, database persistence, unit testing, serialization, and security.

## 10k Architecture Overview

The application implements a multiplayer chess server and a command line chess client.

[![Sequence Diagram](10k-architecture.png)](https://sequencediagram.org/index.html#initialData=C4S2BsFMAIGEAtIGckCh0AcCGAnUBjEbAO2DnBElIEZVs8RCSzYKrgAmO3AorU6AGVIOAG4jUAEyzAsAIyxIYAERnzFkdKgrFIuaKlaUa0ALQA+ISPE4AXNABWAexDFoAcywBbTcLEizS1VZBSVbbVc9HGgnADNYiN19QzZSDkCrfztHFzdPH1Q-Gwzg9TDEqJj4iuSjdmoMopF7LywAaxgvJ3FC6wCLaFLQyHCdSriEseSm6NMBurT7AFcMaWAYOSdcSRTjTka+7NaO6C6emZK1YdHI-Qma6N6ss3nU4Gpl1ZkNrZwdhfeByy9hwyBA7mIT2KAyGGhuSWi9wuc0sAI49nyMG6ElQQA)

## Modules

The application has three modules.

- **Client**: The command line program used to play a game of chess over the network.
- **Server**: The command line program that listens for network requests from the client and manages users and games.
- **Shared**: Code that is used by both the client and the server. This includes the rules of chess and tracking the state of a game.

## Starter Code

As you create your chess application you will move through specific phases of development. This starts with implementing the moves of chess and finishes with sending game moves over the network between your client and server. You will start each phase by copying course provided [starter-code](starter-code/) for that phase into the source code of the project. Do not copy a phases' starter code before you are ready to begin work on that phase.

## IntelliJ Support

Open the project directory in IntelliJ in order to develop, run, and debug your code using an IDE.

## Maven Support

You can use the following commands to build, test, package, and run your code.

| Command                    | Description                                     |
| -------------------------- | ----------------------------------------------- |
| `mvn compile`              | Builds the code                                 |
| `mvn package`              | Run the tests and build an Uber jar file        |
| `mvn package -DskipTests`  | Build an Uber jar file                          |
| `mvn install`              | Installs the packages into the local repository |
| `mvn test`                 | Run all the tests                               |
| `mvn -pl shared test`      | Run all the shared tests                        |
| `mvn -pl client exec:java` | Build and run the client `Main`                 |
| `mvn -pl server exec:java` | Build and run the server `Main`                 |

These commands are configured by the `pom.xml` (Project Object Model) files. There is a POM file in the root of the project, and one in each of the modules. The root POM defines any global dependencies and references the module POM files.

## Running the program using Java

Once you have compiled your project into an uber jar, you can execute it with the following command.

```sh
java -jar client/target/client-jar-with-dependencies.jar

♕ 240 Chess Client: chess.ChessPiece@7852e922
```

## Link to server design Phase 2

https://sequencediagram.org/index.html?presentationMode=readOnly#initialData=IYYwLg9gTgBAwgGwJYFMB2YBQAHYUxIhK4YwDKKUAbpTngUSWDABLBoAmCtu+hx7ZhWqEUdPo0EwAIsDDAAgiBAoAzqswc5wAEbBVKGBx2ZM6MFACeq3ETQBzGAAYAdAE5M9qBACu2AMQALADMABwATG4gMP7I9gAWYDoIPoYASij2SKoWckgQaJiIqKQAtAB85JQ0UABcMADaAAoA8mQAKgC6MAD0PgZQADpoAN4ARP2UaMAAtihjtWMwYwA0y7jqAO7QHAtLq8soM8BICHvLAL6YwjUwFazsXJT145NQ03PnB2MbqttQu0WyzWYyOJzOQLGVzYnG4sHuN1E9SgmWyYEoAAoMlkcpQMgBHVI5ACU12qojulVk8iUKnU9XsKDAAFUBhi3h8UKTqYplGpVJSjDpagAxJCcGCsyg8mA6SwwDmzMQ6FHAADWkoGME2SDA8QVA05MGACFVHHlKAAHmiNDzafy7gjySp6lKoDyySIVI7KjdnjAFKaUMBze11egAKKWlTYAgFT23Ur3YrmeqBJzBYbjObqYCMhbLCNQbx1A1TJXGoMh+XyNXoKFmTiYO189Q+qpelD1NA+BAIBMU+4tumqWogVXot3sgY87nae1t+7GWoKDgcTXS7QD71D+et0fj4PohQ+PUY4Cn+Kz5t7keC5er9cnvUexE7+4wp6l7FovFqXtYJ+cLtn6pavIaSpLPU+wgheertBAdZoFByyXAmlDtimGD1OEThOFmEwQZ8MDQcCyxwfECFISh+xXOgHCmF4vgBNA7CMjEIpwBG0hwAoMAADIQFkhRYcwTrUP6zRtF0vQGOo+RoARiqfKR3y-P8gKoQ2oEVEBzzgeWKlaSC6k7AWtGYHpUAVG+XYIEJ4oYoJwkEkSYCkrZFTDvytSMiybLKVyN40vuFTLmKEpujKcplu8SqYCqwYam6RgQGoaAAOTMFaaLbigAA8pTefSPZ9uhUCFbZtQAJJoFQJpIBwbpRjGcYicg5iVdUzzpvhoxjDmqh5vM0FFiW9TivVyDroFDb0XlhXFaOKJgD47wbu6W6eeUVVTY1cgoE0+h-DsGKmQCpKJeqMCTQ167nQxoFdZ2NV1XdR1bDsLUoLGClFB1GDPTUtTpgAjARg3DeZY3QBNb3TTAD1zU221LWOE4oM+8Tnpe15LWFwqPgGuNblZQNIs54oZKoAHlYVVm1AZcVzKpsGXlR6CqVcolA88uF9UznLmWRYwURzyFc42DGeN4fj+F4KDoDEcSJArSvOb4WCiYKoH1A00gRvxEbtBG3Q9HJqgKQRYuIfWks6eUDPjDb1GS1ZNnOnZQma053unq5ajuXlXm3j5flYzj8G22gc4hSOBOiuK656oYIDQCi4DEy+W5XclWpoBAzD2fYjLruKMCWEyC1FaHJW9v2T2lFVzLTJe0BIAAXigHDfb98Y8033V1GDEP8lDo3FrDCqt3q7ddzNAzI49nuLbXy1MmtaBZ1eW2eyHcc+RwKDcMel4YljeNrwn0jH0yhgp9vHpk4PL0a-7-4IFgjdO9zANgLzdR+ZZjok2GWzF-AonXP4bA4oNT8TRDAAA4kqDQ2txLA0aIg42Zt7BKmtuzaOwttJD10o8OEjNRYENdlpaEZDKAexesgHIyCcxOTRAHEkwdyho3DmfF26BY68njuUcKScYAPzTsWFAmcL451VHnDCBci5CVLjdLelcv4rxrgfOuZVG7NxnvEOe3de5tX+iUf+L9gYj36pDfME9xrTwosYhelAl7VzRitTej9d6dn3kInyTCwAsLUBiQRC5VAJwiuuE0CAYAgDWiiUguCcweLXrUIJMAIAADMYApMMBAHQAAraRYANDPyqvA5hKDqa02-nQuoIxlj5NUAWBo4x8nVWkEQkEmx4i6hQG6IWrNljJFAGqIZkERljHyQAOSmTQmAnRf4WIAbUIBoxmkoLaR0pUXSenLD6QMyZRkYKjIQOMk5I1jJbLmPM05lwlkgOlkxOWHAADsbgnAoCcDECMwQ4BcQAGzwAxkgis5jzA6yHnrVoHQcF4P6vwiWNCCJzIWRZB2TtKFR2oWcmZSp7nXIsu7Hant0ZHhQCEjEcAMYcKDqjdJvCzzIvCaFERwponiPiKndOpSfHyASvIjaMAlEwGLqo8uGi0k6NHKVBuQ9yYuhbs4qAncTHRh+mYgeusbHZjHvYwsk9Sw+EMS42K7jbKr1lbULx61ZGCsZTaw8B1qXormGy4RD41wCmAKKlAmw8kVnFJAblhgtDyD0AYSyDSlVdlpZSkJtTP501KNizp3TJY6phRs3Zcx9mS3mmAuWlc+wQEDbEJACQwClvsoGgAUhAcuISYhjJAGqSFZRfQwsaE0ZkMkej5Pwbiu2qL+rYAuZXKAcAID2SgAcgl+bM00PKqQ2E+kcWUUIdMidwAp0zrnQujNRDaHrusmSl6RSm1oGpY2qmKBCSBw8nvbhTKmQR1ZcFAJ6goliIkXymRJNBW5xFWKiV3c1EVyrla7R365X11TQY1V6qe6ar7u1VZViepOHBrYg1xKYYmrNWq+eFrnkyrg7aje9qgPAC4Twpkbq9nSE9fyBOGQLCoBoGGoNcwhVJXBXMVK6UsowByjkCjETajysQ+SgAQiGelpi-rZoksPPCo9cyGrGIR+o7R4hyF4+GtKqhMrZWtDkS1WjPHUa3iE18Wiqp+AjVSpUGIM2kgqeSu9N6lTJs0Wp+mDTGYrM6lhwBGnRjkeLQELwe7lZVtVlAeLiBgywGANgCdhA8gFBgJ2sS3a1N6wNkbE2ZtjDANXY7YLIxT1fgYVQJEIBuB4DCfR9JR8T6Yz7G1-GHLag3y676vsajsnQGOG1SDD8I26H0GILzL1Ut4H86mn++W1m5vI0AA
