<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Tracking</title>
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
    		<c:when test="${connecte}">
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
          		<form method="post" action="<c:url value="/EspacePublic" />">
			    	<h2><span>Tracking</span></h2>
			        <p>Vous avez la possibilité de consulter l'état d'une commande grâce à sa référence. </p>
			        <!--<c:if test="${empty sessionScope.sessionUtilisateur && !empty requestScope.intervalleConnexions}">
			        <p class="info">(Vous ne vous êtes pas connecté(e) depuis ce navigateur depuis ${requestScope.intervalleConnexions})</p>
			        </c:if>-->
			 
			        <label for="reference">Référence du colis<span class=asterisque >*</span> :</label>
			        <input type="text" id="reference" name="reference" value="<c:out value="${reference}"/>" size="60" maxlength="60" /> <!--  value="<c:out value="${ }"/>" -->
			        <span class="erreur">${form.erreurs['reference']}</span><br />
			       <!--  
			       	Permet d'ajouter une vérification sur l'adresse mail (+ voir le form associé)
			        <label for="mail">E-mail du client<span class=asterisque >*</span> :</label>
			        <input type="text" id="mail" name="mail" size="60" maxlength="60" />
			        <span class="erreur">${form.erreurs['mail']}</span><br />
			        -->
			        <input type="submit" value="Consulter" title="Consulter" /></br>
			        
			                 
			       	<%-- Vérification de la présence d'un objet utilisateur en session 
			        <c:if test="${!empty sessionScope.sessionUtilisateur}">
			        Si l'utilisateur existe en session, alors on affiche son adresse email. 
			        <p class="succes">Vous êtes connecté(e) avec l'adresse : ${sessionScope.sessionUtilisateur.email}</p>
			        </c:if>--%>
        		
               		<c:choose>
	        	 	<c:when test="${fn:length(trackingList) == 0}"> 
	        	 		<c:if test="${recherche }">
	        	 			</br></br>Aucun élément n'a été trouvé.
	        	 		</c:if>
	        	 		
	        	 	</c:when>
	        	 	<c:otherwise>
	        	 	</br>
	        	 	<span class="titre">Tracking du colis <c:out value="${reference}" /></span>
               		<table class="historique">
               			<thead> <!-- En-tête du tableau -->
								   <tr>
									   <th>Etape</th>
									   <th>Date</th>
									   <th>Opération</th>
									   <th>Centre</th>
									   <th>Réseau étranger</th>
								   </tr>
						</thead>
						<tbody> <!-- Corps du tableau -->
					     <c:forEach var="tracking" items="${trackingList}" varStatus="vs" >
					    	<tr>
					      		<td><c:out value="${vs.count}"/> </td>
					      		<td><c:out value="${tracking.date}" /></td>
					      		<c:choose>
			 						<c:when test = "${ tracking.type_ope == '1'}">
			 							<td>Prise en charge du colis</td>
			 						</c:when>
			 						<c:when test = "${ tracking.type_ope == '2'}">
			 							<td>Absence du client</td>
			 						</c:when>
			 						<c:when test = "${ tracking.type_ope == '3'}">
			 							<td>Refus du colis</td>
			 						</c:when>	
			 						<c:when test = "${ tracking.type_ope == '4'}">
			 							<td>Livraison au destinataire</td>
			 						</c:when>
			 						<c:when test = "${ tracking.type_ope == '5'}">
			 							<td>Abscence du destinataire</td>
			 						</c:when>
			 						<c:when test = "${ tracking.type_ope == '6'}">
			 							<td>Restitution au client</td>
			 						</c:when>
			 						<c:when test="${ tracking.type_ope == '7'}">
			 							<td>Réception du colis</td>
			 						</c:when>
			 						<c:when test="${ tracking.type_ope == '8'}">
			 							<td>Arrivée de l'étranger</td>
			 						</c:when>
			 						<c:when test = "${ tracking.type_ope == '11'}">
			 							<td>Arrivée du chariot</td>
			 						</c:when>
			 						<c:when test = "${ tracking.type_ope == '12'}">
			 							<td>Départ du chariot</td>
			 						</c:when>
			 						<c:when test = "${ tracking.type_ope == '13'}">
			 							<td>Scellage du chariot</td>
			 						</c:when>
			 						<c:when test = "${ tracking.type_ope == '14'}">
			 							<td>Déscellage du chariot</td>
			 						</c:when>
			 						<c:when test = "${ tracking.type_ope == '15'}">
			 							<td>Départ vers l'étranger</td>
			 						</c:when>
			 						<c:when test = "${ tracking.type_ope == '16'}">
			 							<td>Placement dans un chariot</td>
			 						</c:when>
			 						<c:when test = "${ tracking.type_ope == '17'}">
			 							<td>Vidage du chariot</td>
			 						</c:when>
 								</c:choose>
 								<c:choose>
			 						<c:when test = "${ tracking.centre == null}">
			 							<c:choose>
					 						<c:when test = "${ tracking.type_ope == '7'}">
					 							<td><c:out value="${tracking.partenaire}" /></td>
					 						</c:when>
					 						<c:otherwise>
					 							<td>-</td>
					 						</c:otherwise>
 										</c:choose>
			 						</c:when>
			 						<c:otherwise>
			 							<c:choose>
					 						<c:when test = "${ tracking.centre == 'Aeroport_Liege'}">
					 							<td>Aéroport de Liège</td>
					 						</c:when>
					 						<c:when test = "${ tracking.centre == 'Aeroport_Bruxelles'}">
					 							<td>Aéroport de Bruxelles</td>
					 						</c:when>
					 						<c:otherwise>
						 						<td><c:out value="${tracking.centre}" /></td>
					 						</c:otherwise>
 										</c:choose>
			 						</c:otherwise>
 								</c:choose>
 								<c:choose>
			 						<c:when test = "${ tracking.partenaire == null}">
			 							<td>-</td>
			 						</c:when>
			 						<c:otherwise>
			 							<c:choose>
					 						<c:when test = "${ tracking.type_ope == '7'}">
					 							<td>-</td>
					 						</c:when>
					 						<c:otherwise>
					 							<td><c:out value="${tracking.partenaire}" /></td>
					 						</c:otherwise>
 										</c:choose>
			 						</c:otherwise>
 								</c:choose>
					      	</tr>
					  </c:forEach>
					</table>
					</c:otherwise>
					</c:choose>
			  </form>
			        
			        
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
