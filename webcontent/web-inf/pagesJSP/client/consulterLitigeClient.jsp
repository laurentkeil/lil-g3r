<!-- Page utilisée par le client pour voir les détails d'un litige et d'y répondre si celui-ci n'est pas fermé -->
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
       <c:import url="/inc/client/menu.jsp" />
       <c:import url="/inc/logo.jsp" />
    </div>
  </div>
   <div class="content">
	<div class="content_resize">
      	<div class="mainbar">
        	<div class="article">
        		<h2><span>Consultation d'un litige</span></h2>
            	</br>
            	
            	<c:if test="${litige.statut == 4 }">
			 				
			 				<p class="imageEtTexte"><img src="inc/images/attention-48.png" alt="attention"/><span><b >Votre litige est fermé.</b></span></p></br>
			 	</c:if>
            	<p> Date de création du litige : <c:out value="${creation}" /></br></br>
            	
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
 				</br></br>
            	<c:forEach var="litige" items="${litige.detail}">
            		<c:choose>
			 			<c:when test = "${ litige.type == '1'}">
			 				</br>
			 				Envoyé par le <span class="gras">client </span>le <c:out value="${litige.date}" />
			 				<div class="arrow_boxClient">
			 					${f:nl2br(litige.description)}
			 					</br>
			 				</div>
			 			</c:when>
			 			<c:when test = "${ litige.type == '2'}">
			 			</br>
			 				Envoyé par le <span class="gras">client </span>le <c:out value="${litige.date}" />
			 				<div class="arrow_boxClient">
			 					${f:nl2br(litige.description)} </br>
			 				</div>
			 				
			 			</c:when>
			 			<c:otherwise>
			 				</br>
			 				<div class = "employe">Envoyé par un <span class="gras">employé</span> le <c:out value="${litige.date}" /></div>
			 				<div class="arrow_boxEmploye">
			 					${f:nl2br(litige.description)}</br>	
			 				</div>		
			 			</c:otherwise>
 					</c:choose>
            	</c:forEach>
            	
            	<c:choose>
			 			<c:when test = "${ litige.isReponse()}">
			 				<c:choose>
			 					<c:when test ="${ litige.statut != 4}">
			 						</br>
					 				<form method="post" action="<c:url value="/ReponseLitige" />" name="formSaisie" onsubmit="return valider()">
						 				<input type="hidden" name="idLitige" value="<c:out value="${litige.id}" />"> 
						 				<input type="hidden" name="type" value="1"> 
						 				<input type="hidden" name="statut" value="<c:out value="2" />"> 
						 				Réponse :<span class="erreur">${form.erreurs['description']}</span> </br>
										<textarea cols="120" rows="20" name="description" id="description" placeholder="Votre répondre ne peut être vide. Le cas échéant, elle ne sera pas envoyée. De plus, les caractères '&lt;', '&gt;' et '&' sont interdits"><c:out value="${litige.description}"/></textarea>
										</p>
										<input type="submit" value="Envoyer" />
										<p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
									</form>
			 					</c:when>
			 					<c:otherwise>
				 					<p class="imageEtTexte"><img src="inc/images/attention-48.png" alt="attention"/><span><b >Votre litige est fermé.</b></span></p>
				 				</c:otherwise>
			 				</c:choose>
			 			</c:when>
			 			<c:when test = "${ litige.statut == 4}">
			 				</br>
			 				<p class="imageEtTexte"><img src="inc/images/attention-48.png" alt="attention"/><span><b >Votre litige est fermé.</b></span></p>
			 			</c:when>
			 			<c:otherwise>
			 				</br>
			 				Vous ne pouvez répondre à ce litige tant qu'un employé n'aura pas répondu à votre précédent message.
			 			</c:otherwise>
 					</c:choose>
            	</p>
		        
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