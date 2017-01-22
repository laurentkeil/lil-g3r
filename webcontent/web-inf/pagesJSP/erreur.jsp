<!-- Page d'erreur pour éviter d'afficher les erreurs directement à l'utilisateur -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Erreur</title>
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
       <div class="menu_nav">
       </div>
      <div class="logo">
	   	<h1><a href="<c:url value="/Accueil"/>"><img src="inc/images/erreur_100.png" alt="" /></a></h1>
	  </div>
      
    </div>
  </div>
  	<div class="content">
	<div class="content_resize">
      	<div class="mainbar">
        	<div class="article">
			<div class="errorPage">
				</br>
				<p><img src="inc/images/error.png" alt="Erreur 404" /></p>
				<p>La page que vous avez demandée n'a pas été trouvée...</p>
				<p>L'URL que vous avez entré est peut-être incorrecte. </p>
				<p><a href="Accueil">Retour vers la page d'accueil</a></p>
			</div>
			</div>
			</div>
			</div>
			<div class="clr"></div>
			</div>
  </div>
  <div class="footer">
		<c:import url="/inc/pied_de_page.jsp" />
  </div>

</body>
</html>
