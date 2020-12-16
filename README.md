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



https://github.com/rudahee/ahorcado