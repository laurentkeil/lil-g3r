<!--  Page pour la consultation des litiges par l'employé -->
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
	
	<script type="text/javascript">
	function fermeture() {
		  // si la valeur du champ prenom est non vide
		  if(document.formFermeture.ferme.value == "non") {
			    alert("Le litige n'a pas été fermé.");
		    	return false;
		    
		  }
		  else {
			alert("Le litige a été fermé, vous allez être redirigé.");
		    return true;
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
        		<h2><span>Consultation du litige n°<c:out value="${litige.id}" /></span></h2>
            	</br>
            	<c:if test="${litige.statut == 4 }">
			 				<!-- Quand un litige est fermé -->
			 				<p class="imageEtTexte"><img src="inc/images/attention-48.png" alt="attention"/><span><b >Le litige est fermé.</b></span></p></br>
			 	</c:if>
            	<p> Date de création du litige : <c:out value="${creation}" /></br></br>
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
 				 <c:choose>
			 		<c:when test="${litige.statut != 4 }">
		 				<form method="post" action="FermetureLitige" name="formFermeture" onsubmit="return fermeture()">
		 				<p class="imageEtTexte">Fermeture du litige <a href="#"><img src="inc/images/interrogation-32.png" alt="interrogation"/><span class="bulle">La fermeture d'un litige 
		 				envoie directement une note au client</br>
		 				Une fois le litige fermé, vous serez redirigé vers la page listant tous les litiges.</span></a> :
		 					<input type="hidden" name="idLitige" value="<c:out value="${litige.id}" />"/> 
							<input type="hidden" name="type" value="4"/> 
							<input type="hidden" name="statut" value="4"/> 
							<input type="hidden" name="mail" value="<c:out value="${litige.mail}" />"/>
							<input type="hidden" name="colis" value="<c:out value="${litige.ICU}" />"/> 
							<input type="hidden" name="idEmpl" value="<c:out value="${idEmpl}" />"/>
			 				<input type= "radio" name="fermeture" value="non" onClick="cacher();" checked> Non 
			 				<input type= "radio" name="fermeture" value="oui" onClick="afficher();"> Oui</br>
			 				
			 				
			 				<p id="champ_cache">
							    Le client est-il tenu responsable ?
							    <input type="radio" name="respon" value="non" checked> Non
							    <input type="radio" name="respon" value="oui"> Oui<br/>
							</p>
							<script type="text/javascript">
								document.getElementById("champ_cache").style.display = "none";
								function afficher() {
									document.getElementById("champ_cache").style.display = "block";
								}
								function cacher() {
									document.getElementById("champ_cache").style.display = "none";
								}
							</script>
			 				<input type="submit" value="Envoyer" /></p>
		 				</form>
 					</c:when></c:choose>
            	<c:forEach var="litige" items="${litige.detail}">
            		<c:choose>
			 			<c:when test = "${ litige.type == '1'}">
			 				</br>
			 				<div class = "employe">Envoyé par le <span class="gras">client </span>le <c:out value="${litige.date}" /></div>
			 				<div class="arrow_boxEmploye">
			 					${f:nl2br(litige.description)}
			 					</br>
			 				</div>
			 			</c:when>
			 			<c:when test = "${ litige.type == '2'}">
			 			</br>
			 				<div class = "employe">Envoyé par le <span class="gras">client </span>le <c:out value="${litige.date}" /></div>
			 				<div class="arrow_boxEmploye">
			 					${f:nl2br(litige.description)} </br>
			 				</div>
			 				
			 			</c:when>
			 			<c:otherwise>
			 				</br>
			 				Envoyé par un <span class="gras">employé</span> le <c:out value="${litige.date}" />
			 				<div class="arrow_boxClient">
			 					${f:nl2br(litige.description)}</br>	
			 				</div>		
			 			</c:otherwise>
 					</c:choose>
            	</c:forEach>
            	
			 	</br>
			 	<c:choose>
			 		<c:when test="${litige.statut == 4 }">
			 				</br>
			 				<p class="imageEtTexte"><img src="inc/images/attention-48.png" alt="attention"/><span><b >Le litige est fermé.</b></span></p>
			 		</c:when>
			 		<c:otherwise>
			 			<form method="post" action="<c:url value="/ReponseLitige" />" name="formSaisie" onsubmit="return valider()">
						 	<input type="hidden" name="idLitige" value="<c:out value="${litige.id}" />"/> 
						 	<input type="hidden" name="type" value="3"/> 
						 	<input type="hidden" name="statut" value="3"/> 
						 	<input type="hidden" name="mail" value="<c:out value="${litige.mail}" />"/>
						 	<input type="hidden" name="colis" value="<c:out value="${litige.ICU}" />"/> 
						 	<input type="hidden" name="idEmpl" value="<c:out value="${idEmpl}" />"/> 
							Réponse :<span class="erreur">${form.erreurs['description']}</span> </br>
							<textarea cols="120" rows="20" name="description" id="description" placeholder="Votre réponse ne peut être vide. Le cas échéant, elle ne sera pas envoyée. De plus, les caractères '&lt;', '&gt;' et '&' sont interdits"><c:out value="${litige.description}"/></textarea>
							
							<input type="submit" value="Envoyer" />
							<p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
						</form>
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