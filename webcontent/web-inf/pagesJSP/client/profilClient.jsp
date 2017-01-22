<!-- Page permettant d'afficher le profil du client avec toutes ses informations personnelles -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Profil</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="inc/css/style2.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="inc/css/coin-slider.css" />
<script type="text/javascript" src="inc/js/cufon-yui.js"></script>
<script type="text/javascript" src="inc/js/cufon-chunkfive.js"></script>
<script type="text/javascript" src="inc/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="inc/js/script.js"></script>
<script type="text/javascript" src="inc/js/coin-slider.min.js"></script>
</head>
<body>
<div class="main">
  <div class="header">
    <div class="header_resize">
       <c:import url="/inc/client/menu.jsp" />
      <c:import url="/inc/logo.jsp" />
    </div>
  </div>
  <div class="content">
	<div class="content_resize">
      	<div class="mainbar">
        	<div class="article">
        		<h2><span>Profil</span></h2></br>
        		<fieldset>
        		<legend class="titre">Informations liées au compte</legend>
                <p>
	                Adresse email : <c:out value="${utilisateur.email}"/>
               		<br />
 					<a href="<c:url value="/HistoriqueCommandes"/>"><u>Historique des commandes</u></a>
                </p>
                </fieldset>
 				</br>
 				<fieldset>
 				<legend class="titre">Informations personnelles</legend>
 				<p>
 					Sexe : 
 					<c:choose>
 						<c:when test = "${ utilisateur.sexe == 'M'}">
 							Homme
 						</c:when>
 						<c:otherwise>
 							Femme
 						</c:otherwise>
 					</c:choose>
	 				<br/>
	 				<br />
	                
	                Nom : <c:out value="${utilisateur.nom}"/> <br />
	                Prénom : <c:out value="${utilisateur.prenom}"/><br />
	                <br/>
	                
	                Pays : <c:out value="${utilisateur.adressePays}"/><br />
	                Code postal : <c:out value="${utilisateur.adresseCode}"/><br />
	                Localité : <c:out value="${utilisateur.adresseLoc}"/><br />
	                
	                Rue : <c:out value="${utilisateur.adresseRue}"/><br />
	                Numéro : <c:out value="${utilisateur.adresseNum}"/><br />
	                <c:if test="${utilisateur.adresseBoite != null}" var="maVariable" scope="session">
				 		Boîte : <c:out value="${utilisateur.adresseBoite}"/><br />
					</c:if>
	                
	                <br/>
                
                	Téléphone portable : <c:out value="${utilisateur.telephonePortable}"/><br />
                	<c:if test="${utilisateur.telephoneFixe != null}" var="maVariable" scope="session">
				 		Téléphone fixe : <c:out value="${utilisateur.telephoneFixe}"/><br />
					</c:if>
                	 <c:if test="${utilisateur.num_tva != null}" var="maVariable" scope="session">
				 		Numéro de TVA : <c:out value="${utilisateur.num_tva}"/><br />
					</c:if>
                
               		Date de naissance : 
               		<c:out value="${utilisateur.jourNaissance}"/> / 
               		<c:out value="${utilisateur.moisNaissance}"/> / 
               		<c:out value="${utilisateur.anneeNaissance}"/><br />
               		<br />
               			Statut du compte : 
               			<c:choose>
	 						<c:when test = "${ statut.statut == '0'}">
	 							Bloqué en raison  
	 							<c:choose>
	 								<c:when test = "${ statut.cause == 'surfacturation'}">
	 									d'une surfacturation non payée endéans les 15 jours.
	 								</c:when>
	 								<c:when test = "${ statut.cause == 'litige'}">
	 								 	d'au moins trois litiges qui ont été considérés comme étant de votre responsabilité.
	 								</c:when>
	 						
	 					</c:choose>
	 						</c:when>
	 						<c:when test = "${ statut.statut == '1'}">
	 							Basique
	 						</c:when>
	 						<c:when test = "${ statut.statut == '2'}">
	 							Avancé
	 						</c:when>
	 						<c:otherwise>
	 							Premium
	 						</c:otherwise>
	 					</c:choose>
               		</br>
               		<br />
               		Il vous est possible de modifier votre profil en cliquant ici : 
               		<a href="<c:url value="/EditerClient"/>"><u>Editer profil</u></a>
 				</p>
 				</fieldset>
        	</div>
      </div>
      <div class="clr"></div>
    </div>
  </div></div>
  <div class="clr"></div>
  <div class="footer">
		<c:import url="/inc/pied_de_page.jsp" />
  </div>

</body>
</html>