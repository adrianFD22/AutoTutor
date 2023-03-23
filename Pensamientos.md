
## Dudas

- [ ] Dudas de la clase Subject
	- [ ] ¿Debería hacer los objetos Subject totalmente inmutables? Actualmente contiene setters pero son package-protected.


## Problemas

- [ ] Problemas de la clase Subject
	- [ ] Crear sesiones (getSessions)
		- [ ] No se alternan bien las sesiones que no son estándar.
	- [ ] Espaciado (this.space)
		- [ ] El espaciado se aplica a toda la asignatura. Sería interesante que se aplicara a distintas sesiones de forma distinta.

- [ ] Problemas de la clase Planning
	- [ ] Crear horarios (getTimetable)
		- [ ] No se crean bien las sesiones de las asignaturas. Hay que almacenar algún tipo de información extra para que, al completar una sesión, se prepare la siguiente para planificar.
		- [ ] ERROR: se ha implementado mal el sistema para espaciar asignaturas.