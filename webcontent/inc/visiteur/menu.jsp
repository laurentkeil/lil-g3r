<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="menu_nav">
	<ul id ="menu" >
		<li>
			<a href="<c:url value="/Accueil"/>">Accueil</a>
		</li>
		<li>
			<a href="<c:url value="/EspacePublic"/>">Tracking</a>
		</li>
		<li>
			<a href="<c:url value="/ContactEntreprise"/>">Infos</a>
			<ul>                          
            	<li><a href="<c:url value="/Tarifs"/>">Tarif</a></li>
            	<li><a href="<c:url value="/NosCentres"/>">Nos centres</a></li>               
            	<li><a href="<c:url value="/ContactEntreprise"/>">Contact</a></li>    
            </ul>
		</li>
		<li><a href="<c:url value="/ConnexionClient"/>">Connexion</a></li>
		<li><a href="<c:url value="/InscriptionClient"/>">Inscription</a></li>
	</ul>
</div>