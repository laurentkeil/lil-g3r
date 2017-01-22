<!-- Page permettant de demander la réinitialisation du mot de passe -->
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
<script type="text/javascript" src="inc/js/script.js"></script>
<script type="text/javascript" src="inc/js/coin-slider.min.js"></script>
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
        		<h2><span>Récupération de mot de passe</span></h2>
        		<c:choose>
        			<c:when test="${confirmation}">
        				Un e-mail vient de vous être envoyé contenant un lien qui vous servira pour réinitialiser votre mot de passe. </br></br>
        				Ce lien expirera dans 24 heures. Si vous ne réinitialisez pas votre mot de passe dans ces délais, vous devrez 
        				recommencer cette manipulation.
        			</c:when>
        			<c:otherwise>
		          		<form method="post" action="<c:url value="/MdpPerduClient" />">
					    	</br><p>Avez-vous oublié votre mot de passe? Entrez votre adresse e-mail ci-dessous et nous vous enverrons un e-mail pour le réinitialiser.</p>
					 
					        <label for="email">Adresse email <span class="requis">*</span></label>
					        <input type="email" id="email" name="email" value="<c:out value="${utilisateur.email}"/>"  placeholder="E-mail" size="20" maxlength="60" />
					        <span class="erreur">${form.erreurs['email']}</span>
					        <br />
					 
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
