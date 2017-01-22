<!-- Page permettant au client de créer un litige sous certains conditions -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Création d'un litige</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="inc/css/style2.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="inc/css/coin-slider.css" />
<script type="text/javascript" src="inc/js/cufon-yui.js"></script>
<script type="text/javascript" src="inc/js/cufon-chunkfive.js"></script>
<script type="text/javascript" src="inc/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="inc/js/script.js"></script>
<script type="text/javascript" src="inc/js/coin-slider.min.js"></script>

<!--  <script type="text/javascript">

	function valider() {
	  // si la valeur du champ prenom est non vide
	  if(document.formSaisie.description.value != "") {
	    // alors on envoie le formulaire
	    if ((document.formSaisie.description.value.indexOf(">") !=-1) || (document.formSaisie.description.value.indexOf("<") != -1) || (document.formSaisie.description.value.indexOf("&") != -1)){
	    	alert("Les caractères \">\", \"<\" et \"&\" sont interdits ! ");
		    return false;
	    }else{
	    	if (document.formSaisie.objet.value == "defaut"){
	    		alert("Vous devez préciser l'objet de votre litige.");
		    	return false;
	    	} else {
	    		alert("Votre réponse a été envoyée correctement.");
		    	return true;
	    	}
	    	
	    }
	    
	  }
	  else {
	    // sinon on affiche un message
	    alert("Veuillez insérer une réponse");
	    return false;
	  }
	}
	
	
	</script>-->
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
	          	<h2><span>Creation d'un litige</span></h2>
          		<div class="clr"></div>
          		<form method="post" action="<c:url value="/CreationLitigeClient" />" >
          		<p>
          		Le problème concerne le colis : <c:out value="${ICU}"/>
          		<input type="hidden" id="colis" name="colis" value="<c:out value="${colis}"/>"/>
          		<input type="hidden" id="ICU" name="ICU" value="<c:out value="${ICU}"/>"/>
                
          		</br></br>
          		 Objet du problème : 
          		<select name="objet">
					<option value="defaut"> Veuillez choisir l'objet de votre problème </option>
					<!--  <option value="1"> Agression physique de la part du coursier </option>
					<option value="2"> Agression verbale de la part du coursier </option>-->
					<option value="1"> Colis endommagé </option>
					<option value="2"> Perte du colis </option>								
					<option value="3"> Refus de la prise en charge du colis </option>
					<option value="4"> Surfacturation </option>
					<option value="5"> Autres </option>			
				</select> 
				<span class="erreur">${form.erreurs['objet']}</span> </br></br>
				 Description du problème :<span class="erreur">${form.erreurs['description']}</span> </br>
				<textarea cols="80" rows="20" name="description" id="description" value="<c:out value="${litige.description}"/>" placeholder="Votre description ne peut être vide. Le cas échéant, elle ne sera pas envoyée. De plus, les caractères '&lt;', '&gt;' et '&' sont interdits"><c:out value="${litige.description}"/></textarea>
				</p>
				<input type="submit" value="Envoyer" class="sansLabel" />
				<p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
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
