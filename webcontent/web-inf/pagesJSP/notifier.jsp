<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Notifier</title>
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
        	  	<c:choose>
	        	 	<c:when test="${!confirm}"> 
	        	 		<form method="post" action="<c:url value="/Notifier" />">
					    	<h2><span>Notifier</span></h2>
					        <p>Vous avez la possibilité de notifier qu'un colis a été livré à l'étranger. </p>
					        <!--<c:if test="${empty sessionScope.sessionUtilisateur && !empty requestScope.intervalleConnexions}">
					        <p class="info">(Vous ne vous êtes pas connecté(e) depuis ce navigateur depuis ${requestScope.intervalleConnexions})</p>
					        </c:if>-->
					 
					        <label for="icu">Référence du colis<span class=asterisque >*</span> :</label>
					        <input type="text" id="icu" name="icu" value="<c:out value="${icu}"/>" size="60" maxlength="60" /> <!--  value="<c:out value="${ }"/>" -->
					        
					        <input type="submit" value="Consulter" title="Consulter" /></br>
	        	 		</form>
	        	 	</c:when>
	        	 	<c:otherwise>
	        	 		</br>
	        	 		</br>
	        	 		Colis reçu correctement à l'étranger.
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
