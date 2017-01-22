<!-- Page pour consulter le profil d'un client par un employé -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Profil du client</title>
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
       <c:import url="/inc/employe/menu.jsp" />
       <c:import url="/inc/logo.jsp" />
    </div>
  </div>
  <div class="content">
	<div class="content_resize">
      	<div class="mainbar">
        	<div class="article">
        		<h2><span>Profil de <c:out value="${client.nom}"/> <c:out value="${client.prenom}"/></span></h2></br>
        		<fieldset>
        		<legend>Informations liées au compte du client</legend>
                <p>
	                Adresse email : <c:out value="${client.email}"/>
               		<br />
 					<a href="<c:url value="/ConsulterCommandeEmploye?mail=${client.email}"/>"><u>Historique des commandes</u></a>
                </p>
                </fieldset>
 				</br>
 				<fieldset>
 				<legend> Informations personnelles du client</legend>
 					<p>
	 					Sexe : 
	 					<c:choose>
	 						<c:when test = "${ client.sexe == 'M'}">
	 							Homme
	 						</c:when>
	 						<c:otherwise>
	 							Femme
	 						</c:otherwise>
	 					</c:choose>
		 				<br/>
		 				<br />
		                
		                Nom : <c:out value="${client.nom}"/> <br />
		                Prénom : <c:out value="${client.prenom}"/><br />
		                <br/>
		                
		                Rue : <c:out value="${client.adresseRue}"/><br />
		                Numéro : <c:out value="${client.adresseNum}"/><br />
		                <c:if test="${client.adresseBoite != null}" var="maVariable" scope="session">
					 		Boîte : <c:out value="${client.adresseBoite}"/><br />
						</c:if>
		                
		                Localité : <c:out value="${client.adresseLoc}"/><br />
		                Code postal : <c:out value="${client.adresseCode}"/><br />
		                Pays : <c:out value="${client.adressePays}"/><br />
		                <br/>
	                
	                	Téléphone portable : <c:out value="${client.telephonePortable}"/><br />
	                	<c:if test="${client.telephoneFixe != null}" var="maVariable" scope="session">
					 		Téléphone fixe : <c:out value="${client.telephoneFixe}"/><br />
						</c:if>
	                	<c:if test="${client.num_tva != null}" var="maVariable" scope="session">
				 			Numéro de TVA : <c:out value="${client.num_tva}"/><br />
						</c:if>
	                
	               		Date de naissance : 
	               		<c:out value="${client.jourNaissance}"/> / 
	               		<c:out value="${client.moisNaissance}"/> / 
	               		<c:out value="${client.anneeNaissance}"/><br />
               			<br />
               			Statut du compte : 
               			<c:choose>
	 						<c:when test = "${ statut.statut == '0'}">
	 							Bloqué en raison  
	 							<c:choose>
	 								<c:when test = "${ statut.cause == 'surfacturation'}">
	 									d'une surfacturation non payée concernant le colis <c:out value="${surfacturation.ICU}"/>
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