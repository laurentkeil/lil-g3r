<!-- Page permettant de lister les problèmes signalés par les coursiers -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Liste de tous les litiges </title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="inc/css/style2.css" rel="stylesheet" type="text/css" />

</head>
<body>
<div class="main">
  <div class="header">
    <div class="header_resize">
       <c:import url="/inc/employe/menu.jsp" />
       <c:import url="/inc/logo.jsp" />
    </div>
  </div>
  <div class="content">
	<div class="content_resize">
      	<div class="mainbar">
        	<div class="articleTab">
        	 	
        		<c:choose>
					<c:when test="${nouveau == '0'}">
						<h2> Liste des nouveaux problèmes signalés par un coursier</h2>
					</c:when>
					<c:otherwise>
						<h2> Liste des problèmes signalés par un coursier et traité</h2>
					</c:otherwise>
				</c:choose>
               
        	 	<c:choose>
	        	 	<c:when test="${fn:length(litigeList) == 0}"> 
	        	 		</br></br>Aucun élément n'a été trouvé.
	        	 	</c:when>
	        	 	<c:otherwise>
	        	 	
	        	 		<c:choose>
							<c:when test="${nouveau == '0'}">
								<form method="post" action="ConsulterLitigeCoursier">
							</c:when>
							<c:otherwise>
								<form method="post" action="ConsulterLitigesCat">
							</c:otherwise>
						</c:choose>
		        	 	<table class="historique">
		               			<thead> <!-- En-tête du tableau -->
										   <tr>
										   	   <c:choose>
										   	   	<c:when test="${nouveau == '0'}">
										   	   		<th>Numéro du problème</th>
										   	   	</c:when>
										   	   	<c:otherwise>
										   	   		<th>Numéro du litige</th>
										   	   	</c:otherwise>
										   	   </c:choose>
										   	   
											   <th>ICU du colis</th>
											   <th>Nom du coursier</th>
											   <th>Prénom du coursier</th>
											   <th>Date de création</th>
											   <th>Objet du litige</th>
											   <th>En cours de traitement</th>
											   <c:if test="${nouveau == '0'}">
											   <th>Supprimer problème</th>
											   </c:if>
										   </tr>
								</thead>
								<tbody> <!-- Corps du tableau -->
							     <c:forEach var="litige" items="${litigeList}">
							    	<tr>
							    		
							    		<c:choose>
										   	   	<c:when test="${nouveau == '0'}">
										   	   		<td>
										      		<input type="submit" value="${litige.numLitigeCoursier}" name="numLitigeCoursier"></td>
										   	   	</c:when>
										   	   	<c:otherwise>
										   	   		<c:choose>
												   	   	<c:when test="${litige.id == null}">
												   	   		<td>Supprimé</td>
												   	   	</c:when>
												   	   	<c:otherwise>
												   	   		<input type="hidden" name="recherche" value="non" />
										   	   				<td><input type="submit" value="${litige.id}" name="numLitige"></td>
										   	   		</c:otherwise>
												   	</c:choose>
										   	   		</c:otherwise>
										   	   </c:choose>
							    		
							      		<td><c:out value="${litige.ICU}" /></td>
							      		<td><c:out value="${litige.nomCoursier}" /></td>
							      		<td><c:out value="${litige.prenomCoursier}" /></td>
							      		<td><c:out value="${litige.dateCreation}" /></td>
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
					 						<c:when test = "${ litige.verrou == '0'}">
					 							<td>Nouveau</td>
					 						</c:when>
					 						<c:when test = "${ litige.verrou == '1'}">
					 							<td>En cours de traitement</td>
					 						</c:when>
					 						<c:when test = "${ litige.verrou == '2'}">
					 							<td>Traité</td>
					 						</c:when>
					 						<c:otherwise>
					 							<td>Traité</td>
					 						</c:otherwise>
		 								</c:choose>
		 								<c:if test="${nouveau == '0'}">
											<td><a href="SupprimerProblCoursier?numLitigeCoursier=<c:out value="${litige.numLitigeCoursier}"/>"><img src="inc/images/croix-16.png" alt="croix"/></a></td>
					    	
										</c:if>
		 								
							      	</tr>
							  </c:forEach>
							</table>
					  </form>
				  </c:otherwise>
        	  </c:choose>
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