<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<div class="sidebar">
    	<h2 class="star"><span>Sidebar</span> Menu</h2>
        <div class="clr"></div>
      	<ul class="sb_menu">
        	<li><a href="<c:url value="/Accueil"/>">Accueil</a></li>
			<li><a href="<c:url value="/EspacePublic"/>">Espace public</a></li>
			<li><a href="<c:url value="/ContactEntreprise"/>">Contact</a></li>
			<li><a href="<c:url value="/ConnexionClient"/>">Connexion</a></li>
			<li><a href="<c:url value="/InscriptionClient"/>">Inscription</a></li>
       </ul>
</div>