<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div class="menu_nav">
	<ul>
		<li>
			<a href="<c:url value="/AccueilEmploye"/>">Accueil</a>
			<ul>                          
            	<li><a href="<c:url value="/Tarifs"/>">Tarifs</a></li>
            	<li><a href="<c:url value="/NosCentres"/>">Nos centres</a></li>
            </ul>
		
		</li>
		<li>
			<a href="<c:url value="/ProfilEmploye"/>">Profil</a>
			<ul>                          
            	<li><a href="<c:url value="/ProfilEmploye"/>">Consulter</a></li>
            </ul>
		</li>
		<li>
			<a href="<c:url value="/ConsulterClientEmploye?mail=all"/>">Client</a>
			<ul>                          
            	<li><a href="<c:url value="/ConsulterClientEmploye?mail=all"/>">Liste</a></li>
                <li>
                	<a href="<c:url value="/ConsulterCommandeEmploye?mail=all"/>">Commandes</a>
                	<ul class="niveau3">
            		 	<li><a href="<c:url value="/ConsulterCommandeEmploye?mail=all"/>">Toutes</a></li>
			            <li><a href="<c:url value="/ConsulterCommandeEmploye?mail=Bel"/>">Nationales</a></li>
			            <li><a href="<c:url value="/ConsulterCommandeEmploye?mail=Ent"/>">Entrantes</a></li>
			            <li><a href="<c:url value="/ConsulterCommandeEmploye?mail=Sor"/>">Sortantes</a></li>
	          		</ul>
                </li>
            	<li><a href="<c:url value="/ClientsBloquesEmploye"/>">Bloqués</a></li>
            	<li>
            		<a href="<c:url value="/ConsulterSurfacturationEmploye?surfact=all"/>">Surfacturations</a>
            		<ul class="niveau3">
            		 	<li><a href="<c:url value="/ConsulterSurfacturationEmploye?surfact=all"/>">Toutes</a></li>
			            <li><a href="<c:url value="/ConsulterSurfacturationEmploye?surfact=p"/>">Payées</a></li>
			            <li><a href="<c:url value="/ConsulterSurfacturationEmploye?surfact=i"/>">Impayées</a></li>
	          		</ul>
            	
            	</li>
            </ul>
		</li>
		<li>
			<a href="<c:url value="/ConsulterLitigesCat?statut=0"/>">Litige</a>
			<ul>                          
            	<!-- <li><a href="<c:url value="/CreationLitigeEmpl"/>">Créer</a></li>-->
                <li><a href="<c:url value="/ConsulterLitigesCat?statut=1"/>">Nouveaux</a></li>
                <li>
                	<a href="<c:url value="/ConsulterLitigesCat?statut=2"/>">Attente HLB</a>
                	<ul class="niveau3">
			            <li><a href="<c:url value="/ConsulterLitigesCat?statut=2"/>">Tous</a></li>
			            <li><a href="<c:url value="/ConsulterLitigesCat?statut=5"/>">Personnels</a></li>
	          		</ul>
                </li>
                <li>
                	<a href="<c:url value="/ConsulterLitigesCat?statut=3"/>">Attente client</a>
                	<ul class="niveau3">
			            <li><a href="<c:url value="/ConsulterLitigesCat?statut=3"/>">Tous</a></li>
			            <li><a href="<c:url value="/ConsulterLitigesCat?statut=6"/>">Personnels</a></li>
	          		</ul>
                </li>
                <li><a href="<c:url value="/ConsulterLitigesCat?statut=4"/>">Fermés</a></li>
                 <li><a href="<c:url value="/ConsulterLitigesCat?statut=0"/>">Tous</a></li>
                  <li>
                  	<a href="<c:url value="/ConsulterLitigeCoursier?nouveau=0"/>">Coursier</a>
                  	<ul class="niveau3">
			            <li><a href="<c:url value="/ConsulterLitigeCoursier?nouveau=0"/>">Nouveaux</a></li>
			            <li><a href="<c:url value="/ConsulterLitigeCoursier?nouveau=1"/>">Traités</a></li>
	          		</ul>
                  </li>
            </ul>
		</li> 
		<li><a href="<c:url value="/DeconnexionEmploye"/>">Deconnexion</a></li>
	</ul>
</div>