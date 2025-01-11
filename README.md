# API Location Outils

##Déploiement
1. Utilisez le script 'ScriptCréationBD' pour créer la BD
2. Utilisez le scrit 'ScriptAjoutDonnéesBD' pour peupler la BD
3. Ouvrez dossier du projet
4. Modifiez le fichier 'application.properties' pour l'adapter à votre BD
5. Faites partir l'application

## Nom
Api de location d'outils / Rent-A-Tool

## Description
Louer des outils de construction avec une API

Fonctionnalités :

L'Api est composé de deux collections: La collection Outil et la collection Utilisateur.

La collection Outil représente les outils à loué. Elle est caractérisé par une image de l'outil, son nom, sa description, son prix, son état, sa disponibilité, ses coordonnés, sa catégorie, sa date de publication et son fournisseur.

La collection Utilisateur réprésente les utilisateurs utilisant l'Api. Elle est caractérisé par une image de l'utilisateur, son nom, son prénom, son numéro de téléphone, son courriel et son rôle.

L'utilisateur peut avoir 2 rôles différents: Client ou Fournisseur.
Un client à le droit de se créer un compte, de le modifier, de le supprimer et d'accéder aux utilisateurs.
Un fournisseur à le droit de se créer un compte, de le modifier, de le supprimer et d'accéder aux données des utilisateurs. De plus il a le droit de créer, modifier et supprimer des outils à son compte.

Tout le monde autant un utilisateur que le publique peuvent accéder à la liste des outils

## Contribution
Binôme #1 : Juan Angel Melgar-Ruiz, Rabbi Miah || Collection : Fournisseur
Binôme #2 : Hamza Ekram, Jean-Christophe Contant || Collection : client

## Auteurs
Hamza Ekram, Jean-Christophe Contant, Juan Angel Melgar-Ruiz, Rabbi Miah. 

## License
API Location Outils © 2024 by Hamza Ekram, Jean-Christophe Contantis licensed under CC BY-NC-SA 4.0 

