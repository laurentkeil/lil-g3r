<!-- Page permettant de lister les litiges au client -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Liste des litiges </title>
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
       <c:import url="/inc/client/menu.jsp" />
       <c:import url="/inc/logo.jsp" />
    </div>
  </div>
  <div class="content">
	<div class="content_resize">
      	<div class="mainbar">
        	<div class="article">
        	  <form method="post" action="ConsulterLitigeClient">
        		<h2><span>Liste de vos litiges </span></h2>
               		
               		<c:choose>
	        	 	<c:when test="${fn:length(litigeList) == 0}"> 
	        	 		</br></br>Aucun élément n'a été trouvé.
	        	 	</c:when>
	        	 	<c:otherwise>
               		<table class="historique">
               			<thead> <!-- En-tête du tableau -->
								   <tr>
									   <th>Numéro du litige</th>
									   <th>Numéro du colis</th>
									   <th>Date de création</th>
									   <th>Date de la </br>dernière opération</th>
									   <th>Objet du litige</th>
									   <th>Statut</th>
								   </tr>
						</thead>
						<tbody> <!-- Corps du tableau -->
					     <c:forEach var="litige" items="${litigeList}">
					    	<tr>
					      		<td><input type="submit" value="${litige.id}" name="numLitige"></td>
					      		<td><c:out value="${litige.ICU}" /></td>
					      		<td><c:out value="${litige.date}" /></td>
					      		<td><c:out value="${litige.dateReponse}" /></td>
					      		<c:choose>
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
 								<c:choose>
			 						<c:when test = "${ litige.statut == '1'}">
			 							<td>Ouvert</td>
			 						</c:when>
			 						<c:when test = "${ litige.statut == '2'}">
			 							<td>En cours de traitement</td>
			 						</c:when>
			 						<c:when test = "${ litige.statut == '3'}">
			 							<td>Traité</td>
			 						</c:when>
			 						<c:otherwise>
			 							<td>Fermé</td>
			 						</c:otherwise>
 								</c:choose>
					      	</tr>
					  </c:forEach>
					</table>
					</c:otherwise>
					</c:choose>
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