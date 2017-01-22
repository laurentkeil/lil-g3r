<!-- Page de confirmation d'inscription pour un client -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Confirmation</title>
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
    	<c:choose>
	 		<c:when test = "${ message == null}">
	 			<c:import url="/inc/visiteur/menu.jsp" />			
	 		</c:when>
	 		<c:otherwise>
	 			<c:import url="/inc/client/menu.jsp" />	
	 		</c:otherwise>
 		</c:choose>
       
      <c:import url="/inc/logo.jsp" />
    </div>
  </div>
  <div class="content">
	<div class="content_resize">
      	<div class="mainbar">
        	<div class="article">
	          	<h2><span>Confirmation d'inscription</span></h2>
	          	<div class="clr"></div>
					<p>
					<c:choose>
	 					<c:when test = "${ message == null}">
	 						Votre inscription a bien été prise en compte, vous pouvez à présent vous connecter en cliquant 
							<a href="<c:url value="/ConnexionClient"/>">ici</a>.
	 					</c:when>
	 					<c:otherwise>
	 						Vous êtes actuellement connecté avec votre compte : <c:out value="${email}"/>, il ne vous est donc pas autorisé
	 						de confirmer un autre compte tant que vous êtes connecté. Veuillez vous déconnecter avant de retenter de 
	 						confirmer votre inscription.
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
