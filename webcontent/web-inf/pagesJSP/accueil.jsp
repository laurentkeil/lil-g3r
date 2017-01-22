<!-- Page d'accueil des visiteurs -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Accueil</title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="inc/css/style.css" rel="stylesheet" type="text/css" />
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
      
      <div class="clr"></div>
      <div class="slider">
        <div id="coin-slider"> 
        	<a href="#"><img src="inc/images/slide1.jpg" width="960" height="360" alt="" />
        		<span><big>Une entreprise à votre écoute</big><br />
          		 <c:import url="/inc/texte/ecoute.jsp" /></span>
          	</a> 
          	<a href="#"><img src="inc/images/slide2.jpg" width="960" height="360" alt="" />
          		<span><big>Un service de livraison rapide</big><br />
          		<c:import url="/inc/texte/livraison.jsp" /></span>
          	</a> 
          <a href="#"><img src="inc/images/slide3.jpg" width="960" height="360" alt="" />
	          <span><big>Appartenance au réseau européen de livraison de colis</big><br />
	          <c:import url="/inc/texte/equipe.jsp" /></span>
          </a> 
        </div>
        <div class="clr"></div>
      </div>
      <div class="clr"></div>
    </div>
  </div>
  <div class="content">
	<div class="content_resize">
      	<div class="mainbar">
        	<div class="article">
          		<h2><span>A propos de</span> HLB-Express</h2>
          		<div class="clr"></div>
          		<p>
	          		 <c:import url="/inc/texte/aPropos.jsp" />
          		</p>
        	</div>
        	<div class="article">
	          	<h2><span>Notre</span> mission</h2>
	          	<div class="clr"></div>
          		<p>
          			<c:import url="/inc/texte/notreMission.jsp" />
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
