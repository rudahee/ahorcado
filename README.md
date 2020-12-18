# Ahorcado

**/!\ Se debe cambiar el `server.address` en el `application.properties`. ¡No pongas localhost!**

El puerto por defecto es: `8082`

## Usando la API REST

Existen 2 endpoints:

Game y User.

----

### **USER**

- **hacer login**
	- **GET** - ip:8082/user

        - Body: JSON
```json
{
	"username": "usuario",
	"password": "contraseñatremendamenteinsegura"
}
```
- **registrarse**
	- **POST** - ip:8082/user

        - Body: JSON
```json
{
	"username": "usuario",
	"password": "contraseñatremendamenteinsegura"
}
```
- **empezar partida**
	- **PUT** - ip:8082/user/id

        - id: Id del jugador

---

### **GAME**
- **probar con una letra**
	- **POST** - ip:8082/game/id
        - id: Id de la partida
        - Body: letra

- **probar con una palabra**
	- **PUT** - ip:8082/game/id
        - id: Id de la partida
        - Body: palabra

- **ver estado de una partida**
	- **GET** - ip:8082/game/id
        - id: Id de la partida

---

## Enunciado

**Descripción:**

En este proyecto debes realizar el juego del ahorcado haciendo uso del patrón MVC,  tests (unitarios y para endpoints) y filtrado de peticiones web.

En este juego un jugador debe acertar una palabra desconocida preguntando en cada paso por una letra. Si la letra no se ha consultado antes y está contenida en la palabra, se muestran todas sus ocurrencias. Si la letra no está contenida o se solicitó anteriormente, se informa del error cometido.

Tras un número determinado de errores, el usuario pierde la partida y se muestra la figura del ahorcado completa. Esta se va mostrando a especie de contador a medida que el número de fallos se incrementa:

<pre>
	________
	|/      |
	|       O
	|     / | \
	|      / \
</pre>

**Funcionalidad mínima:**
- ~~La palabra a adivinar puede ser proporcionada desde la misma ip que levanta el servidor, pero no desde otra~~
  - *La palabra se obtiene por un requisito opcional.*

- ~~El jugador sólo puede preguntar por una letra en cada petición y sólo puede acceder desde una ip diferente a la que levanta el servidor del juego~~

- ~~Se debe incluir un usuario o registro asociado a cada ip para que no colisionen peticiones de diferentes jugadores~~
  - *Por el multijugador que habia pensado, varios jugadores podrian tener la misma partida, asi que a veces es posible interferir en otras partidas, `solo esta resuelto parcialmente.`*

- ~~Las respuestas deben darse en formato json~~

- Incluir tests unitarios y a los endpoints -  **/!\ PRIORIDAD**

**Opcional (nota)**:
- Almacenar un histórico de jugadas y ránking de jugadores
  - *Solo hay incluido un logger.*
- ~~incluir registro de usuario/pass en las peticiones~~
  - *Realizando un login previo*
- ~~seleccionar palabra aleatoria de un banco de datos (descargado o consumido mediante api rest).~~
- Incluir tests end-to-end

https://github.com/rudahee/ahorcado