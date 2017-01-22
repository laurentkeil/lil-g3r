<!-- Page permettant la réinitialisation du mot de passe -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Mot de passe perdu</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="inc/css/style2.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="inc/css/coin-slider.css" />
<script type="text/javascript" src="inc/js/cufon-yui.js"></script>
<script type="text/javascript" src="inc/js/cufon-chunkfive.js"></script>
<script type="text/javascript" src="inc/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="inc/js/jquery-1.9.1.js"></script> 	
<script type="text/javascript" src="inc/js/script.js"></script>
<script type="text/javascript" src="inc/js/coin-slider.min.js"></script>

<link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css"/>
<script src="http://code.jquery.com/jquery-1.9.1.js"></script>
<script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>

</head>
<body>
<div class="main">
  <div class="header">
    <div class="header_resize">
       <c:import url="/inc/visiteur/menu.jsp" />
       <c:import url="/inc/logo.jsp" />
    </div>
  </div>
  <div class="content">
	<div class="content_resize">
      	<div class="mainbar">
        	<div class="article">
        	
				<script src="inc/js/InscriptionFormDynamique.js"></script> <%-- Appel aux fonctions jQuery rendant le formulaire et les vérifications dynamique --%>
        	
        		<h2><span>Réinitialisation de mot de passe</span></h2>
        		<c:choose>
        			<c:when test="${delai == false}">
        				Le lien a expiré, veuillez réintroduire une demande de réinitialisation de mot de passe en cliquant ici :  <a href="<c:url value="/MdpPerduClient"/>">Mot de passe oublié</a></br>
        				
        			</c:when>
        			<c:when test="${confirmation}">
        				Votre mot de passe a bien été modifié.
        			</c:when>
        			<c:otherwise>
		          		<form method="post" action="<c:url value="/Reinitialisation" />" name = "formReinit" id="formReinit">
					    	</br><p>Veuillez introduire votre nouveau mot de passe ainsi que sa confirmation </p>
					 
					 		<span class="imageEtTexte"><label for="motdepasse">Mot de passe<span class=asterisque >*</span> 
			                 			<a href="#"><img src="inc/images/interrogation-32.png" alt="interrogation"/><span class="bulle">
							        					<u>Critères de mot de passe :</u></br></br>
														<b>Faible</b> : contient moins de 5 caractères et aucune combinaison de chiffres, de minuscules ou de majuscules.</br>
														<b>Moyen</b> : contient au moins 5 caractères et au moins deux des éléments suivants: un chiffre, une minuscule, une majuscule.</br>
														<b>Fort</b> : contient au moins 8 caractères et au moins une minuscule et deux des éléments suivants: un chiffre, une majuscule, un caracactère spécial.
										</span></a>
										 :</label>
			                <input type="password" id="motdepasse" name="motdepasse" value="" placeholder="Mot de passe" size="20" maxlength="40" />               
			                <span class="error"></span>
			                </span>
			                <span class="mdpbar"></span>
			                <span class="erreur">${form.erreurs['motdepasse']}</span>
			                <br />
			 
			                <label for="confirmation">Confirmation du mot de passe<span class=asterisque >*</span> :</label>
			                <input type="password" id="confirmation" name="confirmation" value="" placeholder="Confirmation" size="20" maxlength="40" />
			                <span class="error"></span>
			                <span class="erreur">${form.erreurs['confirmation']}</span>
			                <br />
			                
					 		<input type="hidden" name="email" value="<c:out value="${mail}" />"> 
					        <input type="submit" value="Réinitialisation du mot de passe" class="sansLabel" />
					        <br />
					                 
					        <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>  
					              
					  	</form>
			        </c:otherwise>
			       </c:choose>
        	</div>
      </div>
      <div class="clr"></div>
    </div>
  </div>
  
  <div class="footer">
		<c:import url="/inc/pied_de_page.jsp" />
  </div>
</div>
</body>
</html>