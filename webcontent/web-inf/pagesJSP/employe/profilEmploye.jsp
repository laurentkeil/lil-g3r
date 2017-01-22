<!-- Page utilisée pour afficher le profil de l'employé avec ses données personnelles -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Profil de l'employe</title>
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
        		<h2><span>Profil</span></h2></br>
        		<fieldset> 
 				<legend class="titre">Informations liées au compte</legend>
                <p>
	                Adresse email : <c:out value="${employe.email}"/>
               		<br />
                </p>
                </fieldset>
 				</br>
 				<fieldset> 
 				<legend class="titre">Informations personnelles</legend>
 				<p>
 					Sexe : 
 					<c:choose>
 						<c:when test = "${ employe.sexe == 'M'}">
 							Homme
 						</c:when>
 						<c:otherwise>
 							Femme
 						</c:otherwise>
 					</c:choose></br>
 					
 					Statut marital : 
 					<c:choose>
 						<c:when test = "${ employe.statutMarital == '1'}">
 							Célibataire
 						</c:when>
 						<c:when test = "${ employe.statutMarital == '2'}">
	 						<c:choose>
		 						<c:when test = "${ employe.sexe == 'M'}">
		 							Marié
		 						</c:when>
		 						<c:otherwise>
		 							Mariée
		 						</c:otherwise>
		 					</c:choose>
 						</c:when>
 						<c:when test = "${ employe.statutMarital == '3'}">
 							<c:choose>
		 						<c:when test = "${ employe.sexe == 'M'}">
		 							Veuf
		 						</c:when>
		 						<c:otherwise>
		 							Veuve
		 						</c:otherwise>
		 					</c:choose>
 						</c:when>
 						<c:when test = "${ employe.statutMarital == '4'}">
 							<c:choose>
		 						<c:when test = "${ employe.sexe == 'M'}">
		 							Divorcé
		 						</c:when>
		 						<c:otherwise>
		 							Divorcée
		 						</c:otherwise>
		 					</c:choose>
 						</c:when>
 					</c:choose>
	 				<br/>
	 				<br />
	                
	                Nom : <c:out value="${employe.nom}"/> <br />
	                Prénom : <c:out value="${employe.prenom}"/><br />
	                <br/>
	                
	                Rue : <c:out value="${employe.adresse_rue}"/><br />
	                Numéro : <c:out value="${employe.adresse_num}"/><br />
	                <c:if test="${employe.adresse_boite != null}" var="maVariable" scope="session">
				 		Boîte : <c:out value="${employe.adresse_boite}"/><br />
					</c:if>
	                
	                Localité : <c:out value="${employe.adresse_loc}"/><br />
	                Code postal : <c:out value="${employe.adresse_code}"/><br />
	                Pays : <c:out value="${employe.pays}"/><br />
	                <br/>
                
                	Téléphone portable: <c:out value="${employe.telephonePortable}"/><br />
                	 <c:if test="${employe.telephoneFixe != null}" var="maVariable" scope="session">
				 		Téléphone fixe : <c:out value="${employe.telephoneFixe}"/><br />
					</c:if></br>
                
               		Date de naissance : 
               		<c:out value="${employe.jourNaissance}"/> / 
               		<c:out value="${employe.moisNaissance}"/> / 
               		<c:out value="${employe.anneeNaissance}"/><br />
               	
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