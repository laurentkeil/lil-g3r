<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="menu_nav">
	<ul>
		<li>
			<a href="<c:url value="/AccueilClient"/>">Accueil</a>
			<ul>                          
            	<li><a href="<c:url value="/ContactEntreprise"/>">Contact</a></li>
            	<li><a href="<c:url value="/NosCentres"/>">Nos centres</a></li>
            </ul>
		
		</li>
		<li>
			<a href="<c:url value="/ProfilClient"/>">Profil</a>
			<ul>                          
            	<li><a href="<c:url value="/ProfilClient"/>">Consulter</a></li>
                <li><a href="<c:url value="/EditerClient"/>">Editer</a></li>
            	<li><a class="carnet" href="<c:url value="/Carnet"/>">Carnet<br><br>d'adresses</a></li>
            </ul>
		</li>
		<li><a href="<c:url value="/CreerCommande"/>">Commande</a>
			<ul>                          
            	<li><a href="<c:url value="/EspacePublic"/>">Tracking</a></li>
            	<li><a href="<c:url value="/CreerCommande"/>">Commander</a></li>
            	<li><a href="<c:url value="/Tarifs"/>">Tarifs</a></li>
            </ul>
		</li>
		<li>
			<a href="<c:url value="/HistoriqueCommandes"/>">Historique</a>
			<ul>                          
            	<li><a href="<c:url value="/HistoriqueCommandes"/>">Commandes</a></li>
                <li><a href="<c:url value="/ConsulterLitigeClient"/>">Litiges</a></li>
                <li><a href="<c:url value="/ListeSurfacturations?statut=0"/>">Surfacturations</a>
                	<ul class="niveau3">
			            <li><a href="<c:url value="/ListeSurfacturations?statut=0"/>">Toutes</a></li>
			            <li><a href="<c:url value="/ListeSurfacturations?statut=1"/>">Payées</a></li>
			            <li><a href="<c:url value="/ListeSurfacturations?statut=2"/>">Impayées</a></li>
	          		</ul>
                </li>
            </ul>
		
		</li>
		<li><a href="<c:url value="/DeconnexionClient"/>">Deconnexion</a></li>
	</ul>
</div>