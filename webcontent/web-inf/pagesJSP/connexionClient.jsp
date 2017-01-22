<!-- Page de connexion d'un client -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Connexion</title>
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
        		<h2><span>Connexion</span></h2>
          		<form method="post" action="<c:url value="/ConnexionClient" />">
			    	<p>Vous pouvez vous connecter via ce formulaire.</p>
			                <!--<c:if test="${empty sessionScope.sessionUtilisateur && !empty requestScope.intervalleConnexions}">
			                    <p class="info">(Vous ne vous êtes pas connecté(e) depuis ce navigateur depuis ${requestScope.intervalleConnexions})</p>
			                </c:if>-->
			 
			        <label for="nom">Adresse email <span class="requis">*</span></label>
			        <input type="email" id="email" name="email" value="<c:out value="${utilisateur.email}"/>" placeholder="E-mail" size="20" maxlength="60" />
			        <span class="erreur">${form.erreurs['email']}</span>
			        <br />
			 
			        <label for="motdepasse">Mot de passe <span class="requis">*</span></label>
			        <input type="password" id="motdepasse" name="motdepasse" value="" placeholder="Mot de passe" size="20" maxlength="20" />
			        <span class="erreur">${form.erreurs['motdepasse']}</span>
			        <br />
			        <a href="<c:url value="/MdpPerduClient"/>">Mot de passe oublié ?</a></br>
			 
			        <input type="submit" value="Connexion" class="sansLabel" />
			        <br />
			                 
			        <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>  
			                
			                 
			        <%-- Vérification de la présence d'un objet utilisateur en session 
			        <c:if test="${!empty sessionScope.sessionUtilisateur}">
			         Si l'utilisateur existe en session, alors on affiche son adresse email. 
			        <p class="succes">Vous êtes connecté(e) avec l'adresse : ${sessionScope.sessionUtilisateur.email}</p>
			        </c:if>--%>
			        </form>
			        
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
