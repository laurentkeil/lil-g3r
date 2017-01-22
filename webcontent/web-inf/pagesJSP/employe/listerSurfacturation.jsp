<!-- Page permettant de lister les surfacturations des clients par l'employé -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Liste des surfacturations </title>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link href="inc/css/style2.css" rel="stylesheet" type="text/css" />
<link rel="stylesheet" type="text/css" href="inc/css/coin-slider.css" />
<script type="text/javascript" src="inc/js/cufon-yui.js"></script>
<script type="text/javascript" src="inc/js/cufon-chunkfive.js"></script>
<script type="text/javascript" src="inc/js/jquery-1.4.2.min.js"></script>
<script type="text/javascript" src="inc/js/script.js"></script>
<script type="text/javascript" src="inc/js/coin-slider.min.js"></script>
<script type="text/javascript">

	function valider() {
	  // si la valeur du champ prenom est non vide
	  	if(document.formRecherche.research.value == "defaut") {
		  alert("L'objet de la recherche doit être précisé");
	      return false;
		}else  if(document.formRecherche.research.value == "1") {
			if (document.formRecherche.rech.value === ""){
				alert("Le champ de recherche ne peut être vide");
				return false;
			}else{
				var reg = new RegExp('([^.@]+)(\\.[^.@]+)*@([^.@]+\\.)+([^.@]+)', 'i');
			  	if(reg.test(document.formRecherche.rech.value)){
					return true;
			 	}else{
			 		alert("Le format de l'adresse mail n'est pas valide");
					return false;
				}
			}
	  }else{
		  	if (document.formRecherche.rech.value === ""){
				alert("Le champ de recherche ne peut être vide");
				return false;
		  	} else{
		  		return true;
		  	}
	  }
	}
	
	</script>
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
        	<div class="article">
        		<c:choose>
        		<c:when test="${surfact == 'all'}">
        			<h2><span>Liste de toutes les surfacturations </span></h2>
        		</c:when>
        		<c:when test="${surfact == 'p'}">
        			<h2><span>Liste des surfacturations payées </span></h2>
        		</c:when>
        		<c:when test="${surfact == 'i'}">
        			<h2><span>Liste des surfacturations impayées </span></h2>
        		</c:when>
        		<c:otherwise>
        			<h2><span>Liste des surfacturations à partir d'une recherche </span></h2>
        		</c:otherwise>
        		</c:choose>
        		
               		
               		<c:choose>
	        	 	<c:when test="${fn:length(surfactList) == 0}"> 
	        	 		</br></br>Aucun élément n'a été trouvé.
	        	 	</c:when>
	        	 	<c:otherwise>
	        	 	<form method ="post" action="ConsulterSurfacturationEmploye" name="formRecherche" onsubmit="return valider()" >
	        	 		</br></br>
			        	 	Recherche :
			        	 	<select name="research">
								<option value="defaut"> Veuillez choisir l'objet de votre recherche </option>
								<option value="1"> Adresse e-mail </option>
								<option value="2"> Nom du client </option>			
							</select> 
							<input type="hidden" name="recherche" value="oui" />
							<c:choose>
			        		<c:when test="${surfact == 'p'}">
			        			<input type="hidden" name="surfact" value="p" />
			        		</c:when>
			        		<c:otherwise>
								<input type="hidden" name="surfact" value="i" />
			        		</c:otherwise>
			        		</c:choose>
							
			        	 	<input type="text" id="rech" name="rech" placeholder="Insérez l'élément recherché" size="50" maxlength="100" />
			        	 	<input type="submit" value="Rechercher" /> </br>
			        	 	
		        	</form>
               		<table class="historique">
               			<thead> <!-- En-tête du tableau -->
								   <tr>
									   <th>E-mail</th>
									   <th>Nom</th>
									   <th>Prénom</th>
									   <th>Date de la surfacturation</th>
									   <th>ICU</th>
									   <th>Prix</th>
									   <th>Détail</th>
								   </tr>
						</thead>
						<tbody> <!-- Corps du tableau -->
					     <c:forEach var="surfact" items="${surfactList}">
					    	<tr id="detail" onclick="document.location='DetailCommandeEmploye?ICU=<c:out value="${surfact.ICU}"/>'">
					      		<td><c:out value="${surfact.mail}" /></td>
					      		<td><c:out value="${surfact.nom}" /></td>
					      		<td><c:out value="${surfact.prenom}" /></td>
					      		<td><c:out value="${surfact.date_notification}" /></td>
 								<td><c:out value="${surfact.ICU}" /></td>
 								<td><c:out value="${surfact.prix_surfact}" /></td>
 								<td><a id="detail" href="DetailCommandeEmploye?ICU=<c:out value="${surfact.ICU}"/>"><input type="button" value="Détail de la commande"></a></td>
					      		</a>
					      	</tr>
					  </c:forEach>
					</table>
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