<!-- Page temporaire permettant d'inscrire un employé pour faciliter les tests de l'application web -->
<%@ page pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>HLB-Express | Inscription</title>
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
        		<h2><span>Inscription</span></h2>
          		<form method="post" action="<c:url value="/InscriptionEmploye" />">
                <p><b>Vous pouvez vous inscrire via ce formulaire</b></br>
                Les champs marqués d'une astérisque (<span class=asterisque >*</span>) sont obligatoire</p>
 				<p class="titre">Informations liées au compte :</p>
                <label for="email">Adresse email<span class=asterisque >*</span> :</label>
                <input type="email" id="email" name="email" value="<c:out value="${utilisateur.email}"/>"placeholder="E-mail" size="20" maxlength="255" />
                <span class="erreur">${form.erreurs['email']}</span>
                <br />
 
                <label for="motdepasse">Mot de passe<span class=asterisque >*</span> :</label>
                <input type="password" id="motdepasse" name="motdepasse" value="" placeholder="Mot de passe" size="20" maxlength="40" />
                <span class="erreur">${form.erreurs['motdepasse']}</span>
                <br />
 
                <label for="confirmation">Confirmation du mot de passe<span class=asterisque >*</span> :</label>
                <input type="password" id="confirmation" name="confirmation" value="" placeholder="Confirmation" size="20" maxlength="40" />
                <span class="erreur">${form.erreurs['confirmation']}</span>
                <br />
 				<p class="titre"> Informations personnelles :</p>
 				<label for="sexe">Sexe :</label>
                <input type="radio" id="sexe" name="sexe" value="M" checked /> Homme
                <input type="radio" id="sexe" name="sexe" value="F" /> Femme
                <br/><br />
                
                <label for="statutMarital"> Statut marital<span class=asterisque>*</span> :</label>
                <select name="statutMarital">
					<option value="defaut"> Veuillez choisir le statut marital </option>
					<option value="1"> Célibataire </option>
					<option value="2"> Marié(e) </option>
					<option value="3"> Veuf(ve) </option>
					<option value="4"> Divorcé(e) </option>	
				</select> </br>
                
                <label for="nom">Nom<span class=asterisque >*</span> : </label>
                <input type="text" id="nom" name="nom" value="<c:out value="${utilisateur.nom}"/>"placeholder="Nom" size="20" maxlength="100" />
                <span class="erreur">${form.erreurs['nom']}</span>
                <br />
                
                <label for="prenom">Prénom<span class=asterisque >*</span> :</label>
                <input type="text" id="prenom" name="prenom" value="<c:out value="${utilisateur.prenom}"/>"placeholder="Prénom" size="20" maxlength="100" />
                <span class="erreur">${form.erreurs['prenom']}</span>
                <br />
                <br/>
                <label for="adresse_rue">Rue<span class=asterisque >*</span> :</label>
                <input type="text" id="adresse_rue" name="adresse_rue" value="<c:out value="${utilisateur.adresseRue}"/>"placeholder="Rue" size="20" maxlength="100" />
                <span class="erreur">${form.erreurs['adresse_rue']}</span>
                <br />
                
                <label for="adresse_num">Numéro<span class=asterisque >*</span> :</label>
                <input type="text" id="adresse_num" name="adresse_num" value="<c:out value="${utilisateur.adresseNum}"/>"placeholder="Numéro" size="20" maxlength="4" />
                <span class="erreur">${form.erreurs['adresse_num']}</span>
                <br />
                
                <label for="adresse_boite">Boîte :</label>
                <input type="text" id="adresse_boite" name="adresse_boite" value="<c:out value="${utilisateur.adresseBoite}"/>"placeholder="Boîte" size="20" maxlength="4" />
                <br />
                
                <label for="adresse_loc">Localité<span class=asterisque >*</span> :</label>
                <input type="text" id="adresse_loc" name="adresse_loc" value="<c:out value="${utilisateur.adresseLoc}"/>"placeholder="Localité" size="20" maxlength="100" />
                <span class="erreur">${form.erreurs['adresse_loc']}</span>
                <br />
                
                <label for="adresse_code">Code postal<span class=asterisque >*</span> :</label>
                <input type="text" id="adresse_code" name="adresse_code" value="<c:out value="${utilisateur.adresseCode}"/>"placeholder="Code postal" size="20" maxlength="11" />
                <span class="erreur">${form.erreurs['adresse_code']}</span>
                <br />
                <label for="adresse_pays">Pays<span class=asterisque >*</span> :</label>
                <input type="text" id="adresse_pays" name="adresse_pays" value="<c:out value="Belgique"/>"placeholder="Pays" size="20" maxlength="100" />
                <span class="erreur">${form.erreurs['adresse_pays']}</span>
                <br />
                <br/>
                <label for="telephoneFixe">Téléphone fixe<span class=asterisque >*</span> :</label>
                <input type="text" id="telephoneFixe" name="telephoneFixe" value="<c:out value="${utilisateur.telephoneFixe}"/>"placeholder="Téléphone fixe" size="20" maxlength="14" />
                <span class="erreur">${form.erreurs['telephoneFixe']}</span>
                <br />
                
                <label for="telephonePortable">Téléphone portable<span class=asterisque >*</span>:</label>
                <input type="text" id="telephonePortable" name="telephonePortable" value="<c:out value="${utilisateur.telephonePortable}"/>"placeholder="Téléphone mobile" size="20" maxlength="14" />
                <span class="erreur">${form.erreurs['telephonePortable']}</span>
                <br />
                </br>
                
                
                <p>
                
                <label for="naissance">Date de naissance<span class=asterisque >*</span> :</label>
                <input type="text" id="jour" name="jour" value="<c:out value="${utilisateur.jourNaissance}" />" placeholder="jj" size="3" maxlength="2"/> / 
                 <input type="text" id="mois" name="mois" value="<c:out value="${utilisateur.moisNaissance}" />" placeholder="mm" size="3" maxlength="2"/> / 
                  <input type="text" id="annee" name="annee" value="<c:out value="${utilisateur.anneeNaissance}"/>"placeholder="aaaa" size="5" maxlength="4"/>
                <span class="erreur">${form.erreurs['annee']}</span>
                <br />
 				</p>
                <input type="submit" value="Inscription" class="sansLabel" />
                <br />
                 
                <p class="${empty form.erreurs ? 'succes' : 'erreur'}">${form.resultat}</p>
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
