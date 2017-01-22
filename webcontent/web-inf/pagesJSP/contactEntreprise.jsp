<!-- Page contenant les informations de l'entreprise, pour les contacter -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Contact</title>
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
    		<c:when test="${contact == 'employe'}">
    			<c:import url="/inc/employe/menu.jsp" />
    		</c:when>
    		<c:when test="${contact == 'client'}">
    			<c:import url="/inc/client/menu.jsp" />
    		</c:when>
    		<c:otherwise>
    			<c:import url="/inc/visiteur/menu.jsp" />
    		</c:otherwise>
    	</c:choose>
      <c:import url="/inc/logo.jsp" />
    </div>
  </div>
  <div class="content">
    <div class="content_resize">
      <div class="mainbar">
        <div class="article">
          <h2><span>Contact</span></h2>
          <div class="clr"></div>
          <p>
				E-mail : 
					<a href="mailto:HLB-Express@domaine.com" Title="Envoyer un e-mail"> HLB-Express@outlook.com </a></br>
					Numéro de téléphone : 081/12.34.56 </br> </br>
					<h2><span>Adresse de l'entreprise</span></h2></br>
					<b>Adresse </b>:</br> 
					HLB-Express </br>
					Rue de la livraison, 10 </br>
					5000 Namur 
				</p>
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
