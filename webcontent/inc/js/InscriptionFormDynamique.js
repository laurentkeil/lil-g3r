/**
 * MISE EN FORME ET VERIFICATIONS DYNAMIQUES DU FORMULAIRE D INSCRIPTION. (jquery et ajax utilisés)
 */

        	// fonction jQuery permettant de rendre le formulaire et les VERIFICATIONS dynamiques.
	        $(document).ready(function(){
	        	//déclaration des variables récupérant les champs du formulaire d'INSCRIPTION.
            	var $email = $('#email'),
	            	$emailConf = $('#emailConf'),
	            	$motdepasse = $('#motdepasse'),
	            	$confirmation = $('#confirmation'),
	            	
	            	$nom = $('#nom'),
	            	$prenom = $('#prenom'),
	            	$adresse_rue = $('#adresse_rue'),
	            	$adresse_num = $('#adresse_num'),
	            	$adresse_boite = $('#adresse_boite'),
	            	$adresse_loc = $('#adresse_loc'),
	            	$adresse_code = $('#adresse_code'),
	            	$adresse_pays = $('#adresse_pays'),
	            	
	            	$telFixe = $('#telephoneFixe'),
	            	$telPortable = $('#telephonePortable'),
	            	$num_tva = $('#num_tva'),
	            	$checkbox = $('#checkbox'),
	            	$datepicker = $('#datepicker'),
	            	
	            	$form = $('#myform');
            		$formReinit = $('#formReinit');

	        	 	/*
	        	 	 *
	        	 	 * INSCRIPTION FORMULAIRE
	        	 	 *
	        	 	 */
            	
	                //on cache les localités pour que le user choisissent d'abord le code postal. idem pays et cp

	                $("input[name=adresse_code]:text").hide();
	                $("input[name=adresse_loc]:text").hide();
	                
	                /* lorsque l'utilisateur veut envoyer le formulaire pour la REINIT du mdp, on reprocède aux vérifications et on submit si c'est ok. */
	                $formReinit.submit(function(){
		            	if(!result){ //mdp robuste
		            		$motdepasse.next(".error").fadeIn('slow').text("Le mot de passe doit être de sécurité forte.");  	                		
		            		$('.mdpbar').hide();
		            		return false;
		            	}
		            	if (result && conf) {
	                		return true;
	                	} else {
	                		$(this).next(".error").fadeIn('slow').text("Certains champs n'ont pas été saisis correctement.");  	
	                		return false;
	                	}
	        	    });
	                
	                /* lorsque l'utilisateur veut envoyer le formulaire pour s'inscrire, on reprocède aux vérifications et on submit si c'est ok. */
	                $form.submit(function(){
		            	if(!result){ //mdp robuste
		            		$motdepasse.next(".error").fadeIn('slow').text("Le mot de passe doit être de sécurité forte.");  	                		
		            		$('.mdpbar').hide();
		            		return false;
		            	}
		            	if(!document.form.lu.checked){
		            		$checkbox.next(".error").fadeIn('slow').text("Vous devez lire et accepter les conditions générales d'utilisation.");
		            		return false;
		            	} else {
		            		$checkbox.next(".error").fadeOut(); 		
		            	}
		            	if (verifierMail($email) && verifierMailConf($emailConf) && result && conf && verifier($nom) && verifier($prenom) && verifier($adresse_pays) && verifierCode($adresse_code, $adresse_pays) && verifier($adresse_loc) 
	                			&& verifierRue($adresse_rue) && verifierNum($adresse_num) && verifierBoite($adresse_boite) && verifierTelFixe($telFixe) && verifierGSM($telPortable)
	                			&& verifierNumTVA($num_tva) && verifierDate($datepicker) ) {
	                		return true;
	                	} else {
	                		$(this).next(".error").fadeIn('slow').text("Certains champs n'ont pas été saisis correctement.");  	
	                		return false;
	                	}
	                	
	        	    });
	        	 	
	                //a chaque changement du mail
	                $email.keyup(function(){
	        	       	verifierMail($(this));
	        	    });
	                $email.change(function(){
	                	verifierMailConf($emailConf);
	        	    });
	                $emailConf.keyup(function(){
	        	       	verifierMailConf($(this));
	        	    });
	                
	                var result = false;
                	var conf = false;
	                
	                /* Validation du mot de passe avec une barre de progression */
	                $motdepasse.keyup(function(){
	        	       	var mdp = $(this).val();
	        	       	mdp = $.trim(mdp);
	        	       	var lettre_min = /[a-z]/;
	        	       	var lettre_maj = /[A-Z]/;
	        	       	var nombre = /[0-9]/;
	        	       	var carac_spec = /[^ \w]/;
	        	       	
	        	        $(".erreur").hide();
	        	       	if(mdp.length != 0){
	        	       		
	        	       		$(this).next(".error").hide();
	        	       		$('.mdpbar').show();	
	        	       		
	        	       		//password fort		(min et maj et nombre OU min et caractere special et nombre OU min et maj et nombre OU min et maj et caractere special ET plus de 5 caractères et moins de 8) / accepté
	        	       		if( ( ( (mdp.match(lettre_min)) && ((mdp.match(lettre_maj))||(mdp.match(carac_spec))) && (mdp.match(nombre)) ) || ( (mdp.match(lettre_min)) && (mdp.match(lettre_maj)) && ( (mdp.match(nombre)) || (mdp.match(carac_spec)) ) ) ) && (mdp.length >= 8) ){
	        	       			$('.mdpbar').animate({width:'20%'}, 200).show();
	        	       			$('.mdpbar').css('background-color', 'green');
	        	       			$('.mdpbar').text('Fort').show();
	        	       			$(this).css({ // on rend le champ vert
		        	    	        borderColor : 'green'
		        	    	    });
	        	       				result = true;
	        	       		}
	        	       		//password moyen	(min et maj OU min et nombre OU maj et nombre ET plus de 5 caractères et moins de 8) / refusé
	        	       		else if( ( ((mdp.match(lettre_min)) && (mdp.match(lettre_maj))) || ((mdp.match(nombre)) && (mdp.match(lettre_maj))) || ((mdp.match(lettre_min)) && (mdp.match(nombre)) ) ) && (mdp.length >= 5) ){
	        	       			$('.mdpbar').animate({width:'15%'}, 200).show();
	        	       			$('.mdpbar').css('background-color', 'orange');
	        	       			$('.mdpbar').text('Moyen').show();
	        	       			$(this).css({ // on rend le champ rouge
		        	    	        borderColor : 'red'
		        	    	    });
	        	       				result = false;
	        	       		} 
	        	       		//password faible	(min ou maj ou nombre et moins de 5 caractères) / pas accepté
	        	       		else if ( ( (mdp.match(lettre_min)) || (mdp.match(lettre_maj)) || (mdp.match(nombre)) ) && (mdp.length < 5)){
		        	       			$('.mdpbar').animate({width:'10%'}, 200).show();
		        	       			$('.mdpbar').css('background-color', 'red');
		        	       			$('.mdpbar').text('Faible').show();
		        	       			$(this).css({ // on rend le champ rouge
			        	    	        borderColor : 'red'
			        	    	    });
		        	       				result = false;
		        	       	}
	        	       	}else{
	        	       		$(this).next(".error").fadeIn('slow').text("Veuillez saisir votre mot de passe.");
	        	       		$('.mdpbar').hide();
        	       				result = false;
	        	       	}
	        	       	
	        	    });
	                
	                $motdepasse.change(function(){
	                	verifierConf($confirmation);
	        	    });
	                //a chaque changement de la confirmation de mot de passe
	                $confirmation.keyup(function(){
	                	verifierConf($confirmation);
	        	    });
	                
	                
	        	 	//a chaque changement du nom
	        	 	$nom.keyup(function(){
	        	       	verifier($(this));
	        	    });
	        	 	//a chaque changement de prenom
	        	 	$prenom.keyup(function(){
	        	       	verifier($(this));
	        	    });
	        	 	
	        	 	//a chaque changement du pays
	        	 	$adresse_pays.change(function(){
            	       	if(verifier($(this))){
            	       		$("input[name=adresse_code]:text").show("slow", function(){}); //affiche le cp lors de la validation du pays	    	    
            	       	} else {
            	       		$("input[name=adresse_code]:text").hide("slow", function(){}); //cache sinon	   
            	       	}  	 
        	       		$("input[name=adresse_loc]:text").hide("slow", function(){}); //cache la localite a chaque changement	  
            	    });
	        	 	
	        	 	/*contient l'iso code du pays encodé dans l'adresse de destination*/
        			var pays = {
        					'Allemagne': 'DE',
        					'Autriche': 'AT',
        					'Belgique': 'BE',
        					'Bulgarie': 'BG',
        					'Chypre': 'CY',
        					'Croatie': 'HR',
        					'Danemark': 'DK',
        					'Espagne': 'ES',
        					'Estonie': 'EE',
        					'Finlande': 'FI',
        					'France': 'FR',
        					'Grande-Bretagne': 'UK',
        					'Grèce': 'EL',
        					'Hongrie': 'HU',
        					'Irlande': 'IE',
        					'Italie': 'IT',
        					'Lettonie': 'LV',
        					'Littuanie': 'LT',
        					'Luxembourg': 'LU',
        					'Malte': 'MT',
        					'Pays-Bas': 'NL',
        					'Pologne': 'PL',
        					'Portugal': 'PT',
        					'R.Tchèque': 'CZ',
        					'Roumanie': 'RO',
        					'Slovaquie': 'SK',
        					'Slovénie': 'SI',
        					'Suisse': 'CH',
        					'Suède': 'SE',
        			};
        			
	        	 	//complète automatiquement le champ code destinataire (Ajax)
        			$adresse_code.autocomplete({
            	 		 source : function(requete, reponse){ // les deux arguments représentent les données nécessaires au plugin
            	 			$.ajax({
            	 		            url : 'http://api.geonames.org/postalCodeSearchJSON', // on appelle le script JSON
            	 		            dataType : 'json', // on spécifie bien que le type de données est en JSON
            	 		            data : {
            	 		            	postalcode_startsWith : $adresse_code.val(), // on donne la chaîne de caractère tapée dans le champ
            	 		                maxRows : 1,
            	 		                country : pays[$adresse_pays.val()], //recherche par pays
            	 		                username : 'sn1p3rx21x'
            	 		            },
            	 		            
            	 		            success : function(donnee){
            	 		                reponse($.map(donnee.postalCodes, function(objet){
            	 		                	$adresse_code.css({ // on rend le champ vert
            	            	    	        borderColor : 'green'
            	            	    	    });
            	 		                	$adresse_code.next(".error").fadeOut('slow');
            	 		                	$adresse_loc.next(".error").fadeIn('slow').text("Vérifier votre localité.");
            	 		                	$("input[name=adresse_loc]:text").show("slow", function(){}); //affiche la localité lors de la validation du code postal
            	            	    	    $(".erreur").hide();
            	            	    	    $adresse_loc.val(objet.placeName); //Met automatiquement la ville correspondante.  	
            	            	    	    
            	            	            if( $adresse_pays.val() == "Belgique" && !$adresse_loc.val().match(/^[A-Za-zàáâãäåçèéêëìíîïðòóôõöùúûüýÿ\-]+$/i) ){
            	            	            	var locdest = $adresse_loc.val().substring($adresse_loc.val().lastIndexOf(" "));
            	            	            	$adresse_loc.val(locdest); //remplace la ville par la sous-ville s'il y en a une.
            	            	            }
            	            				valid = true;
            	 		                	return objet.postalCode; // on retourne le code postal
            	 		               		
            	 		                }));
            	 		            }
            	 		        });
            	 		 }, //liste de suggestion
            	 		 
            	 	    minLength : 3, // au moins 3 caractères pour afficher l'autocomplétion
            	 	   	position : {
            	 		         my : 'top',
            	 		         at : 'bottom'
            	 		}
            	 	});
        			
        			//on vérifie le champ code postal au changement du champ	
        			$adresse_code.keyup(function(){
            	       	verifierCode($(this), $adresse_pays);
            	    });
        			//a chaque changement du code postal
        			$adresse_code.change(function(){
            	       	verifierCode($(this), $adresse_pays);
            	    });

        			var nameStartExp = $adresse_loc.val();
        			
            	 	//complète automatiquement le champ (Ajax)
        			$adresse_loc.autocomplete({
            	 		 source : function(requete, reponse){ // les deux arguments représentent les données nécessaires au plugin
            	 			$.ajax({
            	 		            url : 'http://api.geonames.org/searchJSON', // on appelle le script JSON
            	 		            dataType : 'json', // on spécifie bien que le type de données est en JSON
            	 		            data : {
            	 		                name_startsWith : nameStartExp, //remplit la liste des villes en fonction du code postal tapé précédement ou non
            	 		                
            	 		                maxRows : 10,
            	 		                country : pays[$adresse_pays.val()],  //recherche par pays
            	 		                username : 'sn1p3rx21x'
            	 		            },
            	 		            
            	 		            success : function(donnee){
            	 		                reponse($.map(donnee.geonames, function(objet){
            	 		                	$adresse_loc.css({ // on rend le champ vert
            	            	    	        borderColor : 'green'
            	            	    	    });
            	 		                	$adresse_loc.next(".error").fadeOut('slow');
            	            				valid = true;
            	 		                    return objet.toponymName; // on retourne la localité
            	 		                }));
            	 		            }
            	 		        });
            	 		 }, //liste de suggestion
            	 		minLength : 0, 
            	 	    position : {
	           	 		         my : 'top',
	        	 		         at : 'bottom'
            	 		}
            	 	});
            	 	
            	 	//a chaque changement de la ville
            	 	$adresse_loc.keyup(function(){
            	       	verifier($(this));
            	    });
            	 	
	        	 	//a chaque changement de la rue
	        	 	$adresse_rue.keyup(function(){
	        	       	verifierRue($(this));
	        	    });
	        	 	//a chaque changement du num
	        	 	$adresse_num.keyup(function(){
	        	       	verifierNum($(this));
	        	    });
	        	 	//a chaque changement de boite
	        	 	$adresse_boite.keyup(function(){
	        	       	verifierBoite($(this));
	        	    });
	        	 	//a chaque changement de téléphone portable
	        	 	$telPortable.keyup(function(){
	        	       	verifierGSM($(this));
	        	    });
	        	 	//a chaque changement de téléphone portable
	        	 	$telFixe.keyup(function(){
	        	       	verifierTelFixe($(this));
	        	    });
	        	 	//a chaque changement de num tva
	        	 	$num_tva.keyup(function(){
	        	       	verifierNumtva($(this));
	        	    });
	        	 	
	        	 	$.datepicker.regional['fr'] = {
	        	 			  closeText: 'Fermer',
	        	 			  prevText: 'Précédent',
	        	 			  nextText: 'Suivant',
	        	 			  currentText: 'Aujourd\'hui',
	        	 			  monthNames: ['Janvier','Février','Mars','Avril','Mai','Juin','Juillet','Août','Septembre','Octobre','Novembre','Décembre'],
	        	 			  monthNamesShort: ['Janv.','Févr.','Mars','Avril','Mai','Juin','Juil.','Août','Sept.','Oct.','Nov.','Déc.'],
	        	 			  dayNames: ['Dimanche','Lundi','Mardi','Mercredi','Jeudi','Vendredi','Samedi'],
	        	 			  dayNamesShort: ['Dim.','Lun.','Mar.','Mer.','Jeu.','Ven.','Sam.'],
	        	 			  dayNamesMin: ['D','L','M','M','J','V','S'],
	        	 			  weekHeader: 'Sem.',
	        	 			  dateFormat: 'dd/mm/yy',
	        	 			  firstDay: 1,
	        	 			  isRTL: false,
	        	 			  showMonthAfterYear: false,
	        	 			  yearSuffix: ''};
	        	 	$.datepicker.setDefaults($.datepicker.regional['fr']);
	        	 	
	        	 	$datepicker.datepicker({
	        	 		minDate: "-150Y",
	        	 		maxDate: "-12Y",
	        	 		showAnim: "fadeIn",
	        	 		changeMonth: true,
	        	 		changeYear: true,
	        	 		yearRange: "-150:-12"
	        	 		//beforeShowDay: $.datepicker.noWeekends
	        	 	});
	        	 	
	        	 	$datepicker.change(function(){
	        	       	verifierDate($(this));
	        	    });
	        	 	
					/*
						Fonction de vérification de formulaire nom, prenom
					*/
            	 	
            	    function verifier(champ){
            	        if(champ.val() == ""){ // si le champ est vide
            	    	    champ.css({ // on rend le champ rouge
            	    	        borderColor : 'red'
            	    	    });
            	    	    champ.next(".error").fadeIn().text("Veuillez entrer une valeur");
            				$(".erreur").hide();
            				return false;
            	        } //match une valeur texte qui peut être composée
            	        else if (!champ.val().match(/^[A-Za-zàáâãäåçèéêëìíîïðòóôõöùúûüýÿ\-\ ]+$/i) ) {
            	        	champ.css({ // on rend le champ rouge
            	    	        borderColor : 'red'
            	    	    });
            	    	    champ.next(".error").fadeIn().text(champ.val() + " n'est pas une valeur valide.");
            				$(".erreur").hide();
            				return false;
            	        }
            	        else{
            	        	champ.css({ // on rend le champ vert
            	    	        borderColor : 'green'
            	    	    });
            	            champ.next(".error").fadeOut('slow');
            	            $(".erreur").hide();
            				return true;
            	        }
            	    }
            	    /*
						Fonction de vérification de formulaire mail
					*/
	        	    function verifierMail(champ){
	        	        if(champ.val() == ""){ // si le champ est vide
	        	    	    champ.css({ // on rend le champ rouge
	        	    	        borderColor : 'red'
	        	    	    });
	        	    	    champ.next(".error").fadeIn().text("Veuillez entrer votre adresse e-mail");
	        				$(".erreur").hide();
	        				return false;
	        	        } 
	                	// Regex utilisé pour le format de l'adresse e-mail
	        	        else if (!champ.val().match(/^[a-z0-9]+([_|\.|-]{1}[a-z0-9]+)*@[a-z0-9]+([_|\.|-]{1}[a-z0-9]+)*[\.]{1}[a-z]{2,6}$/i)) { //si le mail n'est pas du bon format.
	        	        	champ.css({ // on rend le champ rouge
	        	    	        borderColor : 'red'
	        	    	    });
	        	    	    champ.next(".error").fadeIn().text("L'adresse e-mail encodée n'est pas valide.");
	        				$(".erreur").hide();
	        				return false;
	        	        }
	        	        else{
	        	        	champ.css({ // on rend le champ vert
	        	    	        borderColor : 'green'
	        	    	    });
	        	            champ.next(".error").fadeOut('slow');
	        	            $(".erreur").hide();
	        	            return true;
	        	        }
	        	    }
            	    /*
						Fonction de vérification de formulaire mail
					*/
	        	    function verifierMailConf(champ){
	        	        if(champ.val() == ""){ // si le champ est vide
	        	    	    champ.css({ // on rend le champ rouge
	        	    	        borderColor : 'red'
	        	    	    });
	        	    	    champ.next(".error").fadeIn().text("Veuillez confirmer votre adresse e-mail");
	        				$(".erreur").hide();
	        				return false;
	        	        } 
	                	// Regex utilisé pour le format de l'adresse e-mail
	        	        else if (!champ.val().match(/^[a-z0-9]+([_|\.|-]{1}[a-z0-9]+)*@[a-z0-9]+([_|\.|-]{1}[a-z0-9]+)*[\.]{1}[a-z]{2,6}$/i)) { //si le mail n'est pas du bon format.
	        	        	champ.css({ // on rend le champ rouge
	        	    	        borderColor : 'red'
	        	    	    });
	        	    	    champ.next(".error").fadeIn().text("L'adresse e-mail encodée n'est pas valide.");
	        				$(".erreur").hide();
	        				return false;
	        	        }
	        	        else if (champ.val() != $email.val()){
	        	        	champ.css({ // on rend le champ rouge
	        	    	        borderColor : 'red'
	        	    	    });
	        	    	    champ.next(".error").fadeIn().text("Les adresses e-mail encodées sont différentes, merci de les saisir à nouveau.");
	        				$(".erreur").hide();
	        				return false;
	        	        }
	        	        else{
	        	        	champ.css({ // on rend le champ vert
	        	    	        borderColor : 'green'
	        	    	    });
	        	            champ.next(".error").fadeOut('slow');
	        	            $(".erreur").hide();
	        	            return true;
	        	        }
	        	    }
	        	    function verifierConf(champ){
		        	    if (champ.val() == ""){
	                		champ.css({ // on rend le champ rouge
	        	    	        borderColor : 'red'
	        	    	    });
	                		champ.next(".error").fadeIn().text("Veuillez confirmer votre mot de passe.");
	        				$(".erreur").hide();
	        				conf = false;
	        	        } else if (champ.val() != $motdepasse.val()){
	                		champ.css({ // on rend le champ rouge
	        	    	        borderColor : 'red'
	        	    	    });
	                		champ.next(".error").fadeIn().text("Les mots de passes encodées sont différentes.");
	        				$(".erreur").hide();
	        				conf = false;
	        	        }
	        	        else{
	        	        	champ.css({ // on rend le champ vert
	        	    	        borderColor : 'green'
	        	    	    });
	        	        	champ.next(".error").fadeOut('slow');
	        	            $(".erreur").hide();
	        	            conf = true;
	        	        }
	        		}
            	    /*
						Fonction de vérification de formulaire de la rue
					*/
            	    function verifierRue(champ){
            	        if(champ.val() == ""){ // si le champ est vide
            	    	    champ.css({ // on rend le champ rouge
            	    	        borderColor : 'red'
            	    	    });
            	    	    champ.next(".error").fadeIn().text("Veuillez entrer un nom de rue");
            				$(".erreur").hide();
            				return false;
            	        } 
            	        else if (!champ.val().match(/^[A-Za-z0-9àáâãäåçèéêëìíîïðòóôõöùúûüýÿ\-\'\ ]+$/i) ) {
            	        	champ.css({ // on rend le champ rouge
            	    	        borderColor : 'red'
            	    	    });
            	    	    champ.next(".error").fadeIn().text(champ.val() + " n'est pas une rue valide.");
            				$(".erreur").hide();
            				return false;
            	        }
            	        else{
            	        	champ.css({ // on rend le champ vert
            	    	        borderColor : 'green'
            	    	    });
            	            champ.next(".error").fadeOut('slow');
            	            $(".erreur").hide();
            	            return true;
            	        }
            	    }
            	    
            	    /*
						Fonction de vérification de formulaire numéro de rue
					*/
            	    function verifierNum(champ){
            	        if(champ.val() == ""){ // si le champ est vide
            	    	    champ.css({ // on rend le champ rouge
            	    	        borderColor : 'red'
            	    	    });
            	    	    champ.next(".error").fadeIn().text("Veuillez entrer un numéro de rue");
            				$(".erreur").hide();
            				return false;
            	        } 
            	        else if ((!champ.val().match(/^[0-9]*$/i)) || (champ.val() > 3000) ) { //si le champ ne contient pas que des chiffres.
            	        	champ.css({ // on rend le champ rouge
            	    	        borderColor : 'red'
            	    	    });
            	    	    champ.next(".error").fadeIn().text(champ.val() + " n'est pas un numéro de rue valide.");
            				$(".erreur").hide();
            				return false;
            	        }
            	        else{
            	        	champ.css({ // on rend le champ vert
            	    	        borderColor : 'green'
            	    	    });
            	            champ.next(".error").fadeOut('slow');
            	            $(".erreur").hide();
            	            return true;
            	        }
            	    }
            	    
            	    /*
						Fonction de vérification de formulaire de la boite postale
					*/
            	    function verifierBoite(champ){
            	        if (!champ.val().match(/^[a-zA-Z0-9]*$/i)  ) {
            	        	champ.css({ // on rend le champ rouge
            	    	        borderColor : 'red'
            	    	    });
            	    	    champ.next(".error").fadeIn().text(champ.val() + " n'est pas une boite postale valide.");
            				$(".erreur").hide();
            				return false;
            	        }
            	        else{
            	        	champ.css({ // on rend le champ vert
            	    	        borderColor : 'green'
            	    	    });
            	            champ.next(".error").fadeOut('slow');
            	            $(".erreur").hide();
            	            return true;
            	        }
            	    }
            	    
            	    /*
						Fonction de vérification de formulaire de la boite postale
					*/
	        	    function verifierNumtva(champ){
	        	        if (!champ.val().match(/^[A-Z0-9]*$/i)  ) {
	        	        	champ.css({ // on rend le champ rouge
	        	    	        borderColor : 'red'
	        	    	    });
	        	    	    champ.next(".error").fadeIn().text(champ.val() + " n'est pas un numéro de TVA valide.");
	        				$(".erreur").hide();
	        				return false;
	        	        }
	        	        else{
	        	        	champ.css({ // on rend le champ vert
	        	    	        borderColor : 'green'
	        	    	    });
	        	            champ.next(".error").fadeOut('slow');
	        	            $(".erreur").hide();
	        	            return true;
	        	        }
	        	    }
            	    
            	    /*
						Fonction de vérification de formulaire du gsm
					*/
            	    function verifierGSM(champ){
            	    	if(champ.val() == ""){ // si le champ est vide
            	    	    champ.css({ // on rend le champ rouge
            	    	        borderColor : 'red'
            	    	    });
            	    	    champ.next(".error").fadeIn().text("Veuillez entrer un numéro de téléphone");
            				$(".erreur").hide();
            				return false;
            	        }  //expression régulière pour matcher le format de téléphone internationnal
            	    	else if ((!champ.val().match(/^(0|\+\d\d|00\d\d)([ -]?[1-9][0-9]{2})([-. /]?[0-9]{2})([-. ]?[0-9]{2}){2}$/i) ) ) {
            	        	champ.css({ // on rend le champ rouge 
            	    	        borderColor : 'red'
            	    	    });
            	    	    champ.next(".error").fadeIn().text(champ.val() + " n'est pas un format de téléphone portable valide.");
            				$(".erreur").hide();
            				return false;
            	        }
            	        else{
            	        	champ.css({ // on rend le champ vert
            	    	        borderColor : 'green'
            	    	    });
            	            champ.next(".error").fadeOut('slow');
            	            $(".erreur").hide();
            	            return true;
            	        }
            	    }
            	    
            	    /*
						Fonction de vérification de formulaire du téléphone fixe
					*/
            	    function verifierTelFixe(champ){
            	    	if(champ.val() == ""){ // si le champ est vide
            	    		champ.css({ // on rend le champ vert
            	    	        borderColor : 'green'
            	    	    });
            	            champ.next(".error").fadeOut('slow');
            	            $(".erreur").hide();
            	            return true;
            	        }  //expression régulière pour matcher le format de téléphone internationnal
            	    	else if ((!champ.val().match(/^(0|\+\d\d|00\d\d)([ -]?[1-9][0-9]?[0-9]?)([-. /]?[0-9]{2})([-. ]?[0-9]{2}){2}$/i) ) ) {
            	        	champ.css({ // on rend le champ rouge 
            	    	        borderColor : 'red'
            	    	    });
            	    	    champ.next(".error").fadeIn().text(champ.val() + " n'est pas un format de téléphone fixe valide.");
            				$(".erreur").hide();
            				return false;
            	        }
            	        else{
            	        	champ.css({ // on rend le champ vert
            	    	        borderColor : 'green'
            	    	    });
            	            champ.next(".error").fadeOut('slow');
            	            $(".erreur").hide();
            	            return true;
            	        }
            	    }
            	    

            	    /*
						Fonction de vérification de formulaire de date
					*/
            	    function verifierDate(champ){
            	    	if(champ.val() == ""){ // si le champ est vide
            	    		champ.css({ // on rend le champ rouge 
            	    	        borderColor : 'red'
            	    	    });
            	    	    champ.next(".error").fadeIn().text("Veuillez indiquer votre date de naissance.");
            	            $(".erreur").hide();
            	            return false;
            	        }  //expression régulière pour matcher le format de date
            	    	else if ((!champ.val().match(/^(((0[1-9])|(1\d)|(2\d)|(3[0-1]))\/((0[1-9])|(1[0-2]))\/(\d{4}))$/i) ) ) {
            	        	champ.css({ // on rend le champ rouge 
            	    	        borderColor : 'red'
            	    	    });alert("ok");
            	    	    champ.next(".error").fadeIn().text(champ.val() + " n'est pas un format de date valide.");
            				$(".erreur").hide();
            				return false;
            	        }
            	        else {
            	        	champ.css({ // on rend le champ vert
            	    	        borderColor : 'green'
            	    	    });
            	            champ.next(".error").fadeOut('slow');
            	            $(".erreur").hide();
            	            return true;
            	        }
            	    }
            	    
            	    /*
						Fonction de vérification de formulaire code postal
					*/
            	    function verifierCode(champ, pays){
            	        if(champ.val() == ""){ // si le champ est vide
            	    	    champ.css({ // on rend le champ rouge
            	    	        borderColor : 'red'
            	    	    }); 
            	        	$("input[name=adresse_loc]:text").hide("slow", function(){});
            	        	$adresse_loc.next(".error").hide();
 		                	nameStartExp = $adresse_loc.val(); //pour faire remplir normalement la localité    	    	    
            	    	    champ.next(".error").fadeIn().text("Veuillez entrer un code postal");
            				$(".erreur").hide();
            				return false;
            	        } 
            	        else if (!champ.val().match(/^[0-9]*$/i) ) { //si le champ ne contient pas que des chiffres.
            	        	champ.css({ // on rend le champ rouge
            	    	        borderColor : 'red'
            	    	    });
            	        	$("input[name=adresse_loc]:text").hide("slow", function(){});
            	        	$adresse_loc.next(".error").hide();
 		                	nameStartExp = $adresse_loc.val(); //pour faire remplir normalement la localité   
            	    	    champ.next(".error").fadeIn().text("Le code postal ne doit contenir que des chiffres.");
            				$(".erreur").hide();
            				return false;
            	        }
            	        else if (pays.val() == "Belgique" && (champ.val() < 1000  || champ.val() >= 10000) ) {
            	        	champ.css({ // on rend le champ rouge
            	    	        borderColor : 'red'
            	    	    });
            	        	$("input[name=adresse_loc]:text").hide("slow", function(){});
            	        	$adresse_loc.next(".error").hide();
 		                	nameStartExp = $adresse_loc.val(); //pour faire remplir normalement la localité
            	    	    champ.next(".error").fadeIn().text(champ.val() + " n'est pas un code postal valide en " + pays.val());
            				$(".erreur").hide();
            				return false;
            	        }
            	        else if ( champ.val() < 100  || champ.val() >= 100000 ) {
            	        	champ.css({ // on rend le champ rouge
            	    	        borderColor : 'red'
            	    	    });
            	        	$("input[name=adresse_loc]:text").hide("slow", function(){});
            	        	$adresse_loc.next(".error").hide();
 		                	nameStartDest = $adresse_loc.val(); //pour faire remplir normalement la localité
            	    	    champ.next(".error").fadeIn().text(champ.val() + " n'est pas un code postal valide pour ce pays");
            				$(".erreur").hide();
            				return false;
            	        }
            	        else{
            	        	champ.css({ // on rend le champ vert
            	    	        borderColor : 'green'
            	    	    });
            	        	$("input[name=adresse_loc]:text").show("slow", function(){});
            	    	    nameStartExp = $adresse_code.val(); //remplit la champ pour la recherche ajax avec le code pour automatiser la recherche
            	            champ.next(".error").fadeOut('slow');
            	            $(".erreur").hide();
            	            return true;
            	        }
            	    }
	        });