<!-- Page permettant de créer un litige à partir d'un problème signalé par un coursier par un employé -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://example.com/functions" prefix="f" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<title>HLB-Express | Consultation de litiges</title>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<link href="inc/css/style2.css" rel="stylesheet" type="text/css" />
	<link rel="stylesheet" type="text/css" href="inc/css/coin-slider.css" />
	<script type="text/javascript" src="inc/js/cufon-yui.js"></script>
	<script type="text/javascript" src="inc/js/cufon-chunkfive.js"></script>
	<script type="text/javascript" src="inc/js/jquery-1.4.2.min.js"></script>
	<script type="text/javascript" src="inc/js/script.js"></script>
	<script type="text/javascript" src="inc/js/coin-slider.min.js"></script>
	<script type="text/javascript">

	function valider() {
	  // si la valeur du champ prenom est non vide
	  if(document.formSaisie.description.value != "") {
	    // alors on envoie le formulaire
	    if ((document.formSaisie.description.value.indexOf(">") !=-1) || (document.formSaisie.description.value.indexOf("<") != -1) || (document.formSaisie.description.value.indexOf("&") != -1)){
	    	alert("Les caractères \">\", \"<\" et \"&\" sont interdits ! ");
		    return false;
	    }else{
	    	alert("Votre réponse a été envoyée correctement.");
	    	return true;
	    }
	    
	  }
	  else {
	    // sinon on affiche un message
	    alert("Veuillez insérer une réponse");
	    return false;
	  }
	}
	
	
	</script>
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
        		<h2><span>Création d'un litige signalé par <c:out value="${litige.nomCoursier}" /> <c:out value="${litige.prenomCoursier}" /> </span></h2>
            	</br>
            	
            	<p> Date de création du litige : <c:out value="${litige.date}" /></br></br>
            	Client concerné : <c:out value="${litige.nom}"/> <c:out value="${litige.prenom}"/><br>
            	Adresse e-mail du client concerné : <c:out value="${litige.mail}"/></br>
            	</br>
            	
            	Colis concerné : <c:out value="${litige.ICU}"/><br></br>
            	Objet du litige :<c:choose>
			 						<c:when test = "${ litige.objet == '1'}">
			 							<td>Colis endommagé</td>
			 						</c:when>
			 						<c:when test = "${ litige.objet == '2'}">
			 							<td>Perte du colis </td>
			 						</c:when>
			 						<c:when test = "${ litige.objet == '3'}">
			 							<td>Refus de la prise en charge du colis</td>
			 						</c:when>	
			 						<c:when test = "${ litige.objet == '4'}">
			 							<td>Surfacturation</td>
			 						</c:when>
			 						<c:otherwise>
			 							<td>Autre</td>
			 						</c:otherwise>
 								</c:choose>
 				</br>
 				</br>
 				Note du coursier : <c:choose>
			 						<c:when test = "${ litige.descriptionCoursier == NULL}">
			 							Aucune</br>
			 						</c:when>
			 						<c:otherwise>
			 							<c:out value="${litige.descriptionCoursier}" /></br>
			 						</c:otherwise>
 								</c:choose>
 				
            	<form method="post" action="<c:url value="/CreationLitigeCoursier" />" name="formSaisie" onsubmit="return valider()">
					<input type="hidden" name="type" value="4"/> 
					<input type="hidden" name="statut" value="3"/> 
					<input type="hidden" name="idLitige" value="<c:out value="${litige.id}" />"/>
					<input type="hidden" name="mail" value="<c:out value="${litige.mail}" />"/>
					<input type="hidden" name="numLitigeCoursier" value="<c:out value="${litige.numLitigeCoursier}" />"/> 
					<input type="hidden" name="colis" value="<c:out value="${litige.colis}" />"/> 
					<input type="hidden" name="icu" value="<c:out value="${litige.ICU}" />"/> 
					<input type="hidden" name="idEmpl" value="<c:out value="${idEmpl}" />"/> 
					<input type="hidden" name="objet" value="<c:out value="${litige.objet}" />"/> 
					
					Description du problème : 
					<c:if test="${litige.id != null }">
						<span class ="asterisque"> ATTENTION : Le litige existe déjà, merci de répondre en conséquence</span>
					</c:if>
					</br>
					<textarea cols="120" rows="20" name="description" id="description" placeholder="Votre description ne peut être vide. Le cas échéant, elle ne sera pas envoyée. De plus, les caractères '&lt;', '&gt;' et '&' sont interdits"><c:out value="${litige.description}"/></textarea>
							
					<input type="submit" value="Envoyer" />
				</form>
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