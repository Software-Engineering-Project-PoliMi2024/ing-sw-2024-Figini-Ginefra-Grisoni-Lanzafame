<h1 align="center">
  <br>
  <a href="https://www.craniocreations.it/prodotto/codex-naturalis">
  <img src="https://azure.wgp-cdn.co.uk/app-table-top-gaming/posts/CODEX_BoardgameShot-Mateusz-Zajda-1-1-1024x681.jpg?&width=1200&height=630&mode=crop&format=webp&webp.quality=40&scale=bothwhDNK9g/banner.png" alt="Codex in Naturalis" width="900" style = "border-radius: 20px
  "></a>
  <br>
  Software Engineering Project 2024
  <br>
  Codex in Naturalis
  <br>
</h1>

<h4 align="center">A digital version of the board game <a href="https://www.craniocreations.it/prodotto/codex-naturalis">Codex in Naturalis</a> made in Java by: <br><br>
  <a href="https://github.com/figinii" target="_blank" style="color: #c1a015">Matteo Figini</a><br><br>
  <a href="https://github.com/PaoloGinefra" target="_blank" style="color: #0f6c97">Paolo Ginefra</a><br><br>
  <a href="https://github.com/dedepivot" target="_blank"style="color: #227701">Samuele Grisoni</a> <br><br>
  <a href="https://github.com/teo-lan" target="_blank"style="color: #c72921">Teo Lanzafame</a></h4>

# <img src="https://d2nvlc4hwtwbz3.cloudfront.net/media/cache/41/aa/41aaf136be88fc7b9c2289b916903a15.jpg" align="left" alt="Start Card render" width="200" style = "border-radius: 20px; float:left; padding: 5px"> <center>Our features</center>

<center>

| Features                     | Base | Advanced |
| ---------------------------- | ---- | -------- |
| Complete ruleset             | ✅   |          |
| Socket                       | ✅   |          |
| RMI                          | ✅   |          |
| TUI                          | ✅   |          |
| GUI                          | ✅   |          |
| Multiple Games               |      | ✅       |
| Persistence                  |      | ✅       |
| Resilience to disconnections |      | ✅       |
| Chat                         |      | ✅       |

</center>

# <img src="https://m.media-amazon.com/images/I/81ere6LTiYL._AC_UF1000,1000_QL80_.jpg" align="right" alt="Start Card render" width="200" style = "border-radius: 20px; float:right; padding: 5px"> <center>How to run the game</center>

**❗Dependencies❗** In order to run our application you need to have installed a Java version >= 21, we suggest to use [this version](https://www.oracle.com/it/java/technologies/downloads/#java21).

### 1. Run the server

You can find the server jar here: [`Server`]()
Once you have downloaded the jar, you can run it with the following command:

```bash
cd path/to/server.jar
java -jar server.jar
```

When the server is running you will see the `ip address` of the server, you will need it to connect the client.

> **❗Warning❗** The server will run on port `1234` for RMI and `12345` Socket, make sure that these ports are available.

### 2. Run the client

You can find the client jar here: [`Client`]()
Once you have downloaded the jar, you can run it with the following command:

```bash
cd path/to/client.jar
java -jar client.jar
```

By running the client you will be asked to choose the type of interface you want to use, you can choose between `TUI` and `GUI`.

> **❗Warning❗**
> The `GUI` has been tested with machines with an `x86` architecture and an Os between `Windows 10/11`, `Debian 12`, `MacOS 14`.

> **❗Warning❗**
> The `TUI` makes an extensive use of emojis, for the best experience we suggest to use a `Windows` terminal, just remember to enable `UTF-8` encodings by running `chcp 65001`.

When the client is running you will be asked to insert the `ip address` of the server, you can find it in the server logs.
