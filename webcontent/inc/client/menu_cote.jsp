<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>


<div class="sidebar">
    	<h2 class="star"><span>Sidebar</span> Menu</h2>
        <div class="clr"></div>
      	<ul class="sb_menu">
        	<li><a href="<c:url value="/AccueilClient"/>">Accueil</a></li>
			<li><a href="<c:url value="/ProfilClient"/>">Profil</a></li>
			<li><a href="<c:url value="/CreerCommande"/>">Commander</a></li>
			<li><a href="<c:url value="/HistoriqueCommandes"/>">Historique</a></li>
			<li><a href="<c:url value="/EspacePublic"/>">Espace public</a></li>
			<li><a href="<c:url value="/ContactEntreprise"/>">Contact</a></li>
			<li><a href="<c:url value="/DeconnexionClient"/>">Déconnexion</a></li>
       </ul>
</div>