/**
 * MISE EN FORME ET VERIFICATIONS DYNAMIQUES DU FORMULAIRE. (jquery et ajax utilisés)
 */

        	// fonction jQuery permettant de rendre le formulaire et les VERIFICATIONS dynamiques.
	        $().ready(function(){
	        	
	        		//déclaration des variables récupérant les champs du formulaire.
	            	var $nomexp = $('#nom_expediteur'),
	            		$prenomexp = $('#prenom_expediteur'),
            			$paysexp = $('#adresse_pays_expediteur'),
	            		$codeexp = $('#adresse_code_expediteur'),
	            		$locexp = $('#adresse_loc_expediteur'),
	            		$rueexp = $('#adresse_rue_expediteur'),
	            		$numexp = $('#adresse_num_expediteur'),
	            		$boiteexp = $('#adresse_boite_expediteur'),
	            		$telexp = $('#tel_expediteur'),
	                    $champ = $('.champ'),

	            		$nomdest = $('#nom_destinataire'),
	            		$prenomdest = $('#prenom_destinataire'),
            			$paysdest = $('#adresse_pays_destinataire'),
            			$codedest = $('#adresse_code_destinataire'),
            			$locdest = $('#adresse_loc_destinataire'),
	            		$ruedest = $('#adresse_rue_destinataire'),
	            		$numdest = $('#adresse_num_destinataire'),
	            		$boitedest = $('#adresse_boite_destinataire'),
	            		$teldest = $('#tel_destinataire'),
	            		

	            		$hauteur = $('#dimension_hauteur'),
	            		$longueur = $('#dimension_longueur'),
	            		$largeur = $('#dimension_largeur'),
	            		$valeurEstimee = $('#valeurEstimee'),
	            		$poids = $('#poids'),
	            		
	                    $envoyer = $('.envoyer');
	        		
		            	/*
		            		Si l'utilisateur veut passer au prochain bloc du formulaire, on vérifie d'abord s'il n'y a pas d'erreur pour bloquer si oui,
		            		et s'il y en a, on redirige sur le block adéquat, sinon on redirige vers les prochain bloc.
		            	*/
	            		jQuery('input[name=detailColis]:button, input[name=modifCol]:button').click(function(){
		            			validExp = true;
		            			validDest = true;
		            			
		            			/*on vérifie tous les champs destinataire*/
		            	       	if(!(verifier($nomdest) && 
			            	       	verifier($prenomdest) &&
			            	       	verifier($paysdest) &&
			            	       	verifierCodeDest($codedest, $paysdest) &&
			            	       	verifier($locdest) &&
			            	       	verifierRue($ruedest) &&
			            	       	verifierNum($numdest) &&
			            	       	verifierBoite($boitedest) &&
			            	       	verifierTelDest($teldest))){
		            	       		validDest = false;
		            	       	}
		            	       	
		            	       	if(!document.formCom.AdresseDefaut[0].checked){ //si ce n'est pas l'adresse par défaut en expediteur
		            	       		/*on vérifie tous les champs expediteur*/
			            	       	if(!(verifier($nomexp) && 
			            	       	verifier($prenomexp) &&
			            	       	verifierCodeExp($codeexp, $paysexp) &&
			            	       	verifier($locexp) &&
			            	       	verifierRue($rueexp) &&
			            	       	verifierNum($numexp) &&
			            	       	verifierBoite($boiteexp) &&
			            	       	verifierTelExp($telexp))){
			            	       		validExp = false;
			            	       	}
			            	       	
		            	       	} else {
		            	       		validExp = true;
		            	       	}
		            	       	
		            	       	if(!validExp){ //on renvoie vers l'erreur  dans le fieldset expedition si un champ n'est pas correct
		            	       		$("div#Colis").hide("slow", function(){});
		                            $("div#adresses").show("slow", function(){});
		                            $("div#noDefault").show("slow", function(){});
		                            $("div#noCarnetExp").show("slow", function(){});
		                            var divId = jQuery(this).val();
		    	                    $("div#"+divId).show("slow", function(){});
		            	       		if(document.getElementById("CarnetExp").value == "aucune"){
			                    		$("div#prec").hide("slow", function(){});
			                    		$("div#suiv").hide("slow", function(){});
		            	       		} else {
			                    		$("div#prec").show("slow", function(){});
			                    		$("div#suiv").show("slow", function(){});
		            	       		}
		            	       		$('.titreCom').next(".error").fadeIn().text("Certains champs n'ont pas été correctement encodés.");
		            	       	} else {
		                            $("div#noDefault").hide("slow", function(){});
		                            $("div#noCarnetExp").hide("slow", function(){});
		            	       	}
		            	       	
		            	       	if(!validDest){ //on renvoie vers l'erreur dans le fieldset destination si un champ n'est pas correct
		            	       		$("div#Colis").hide("slow", function(){});
		                            $("div#adresses").show("slow", function(){});
		                            $("div#noCarnetDest").show("slow", function(){});
		                            var divId = jQuery(this).val();
		    	                    $("div#"+divId).show("slow", function(){});
		            	       		if(document.getElementById("CarnetDest").value == "aucune"){
			                    		$("div#precDest").hide("slow", function(){});
			                    		$("div#suivDest").hide("slow", function(){});
		            	       		} else {
			                    		$("div#precDest").show("slow", function(){});
			                    		$("div#suivDest").show("slow", function(){});
		            	       		}
		            	       		$('.titreCom').next(".error").fadeIn().text("Certains champs n'ont pas été correctement encodés.");
		            	       	} else {
		                            $("div#noCarnetDest").hide("slow", function(){});
		            	       	}

		            	       	if(validExp && validDest){ //sinon on affiche le prochain fieldset (info colis)
		            	       		$('.titreCom').next(".error").fadeOut();
		            	       		$("div#Colis").show("slow", function(){});
			                        $("div#adresses").hide("slow", function(){});
			                        var divId = jQuery(this).val();
			                        $("div#"+divId).show();
		            	       	}
		                            
		            			return validDest;
	            		});
	        			
	            		
	            		/*EXPEDITEUR*/
	            		
	            		
	        			//a chaque changement du formulaire, on vérifie le champ en question. (nom, prenom)
	            	 	$champ.keyup(function(){
	            	       	verifier($(this));
	            	    });
	        			
	        			//complète automatiquement le champ code expediteur (Ajax)
	            	 	$codeexp.autocomplete({
	            	 		 source : function(requete, reponse){ // les deux arguments représentent les données nécessaires au plugin
	            	 			$.ajax({
	            	 		            url : 'http://api.geonames.org/postalCodeSearchJSON', // on appelle le script JSON
	            	 		            dataType : 'json', // on spécifie bien que le type de données est en JSON
	            	 		            data : {
	            	 		            	postalcode_startsWith : $codeexp.val(), // on donne la chaîne de caractère tapée dans le champ
	            	 		                maxRows : 1,
	            	 		                country : 'BE',
	            	 		                username : 'sn1p3rx21x'
	            	 		            },
	            	 		            
	            	 		            success : function(donnee){
	            	 		                reponse($.map(donnee.postalCodes, function(objet){
	            	 		                	$codeexp.css({ // on rend le champ vert
	            	            	    	        borderColor : 'green'
	            	            	    	    });
	            	 		                	$codeexp.next(".error").fadeOut('slow');
	            	 		                	$locexp.next(".error").fadeIn('slow').text("Vérifier votre localité.");
	            	 		                	$("input[name=adresse_loc_expediteur]:text").show("slow", function(){}); //affiche la localité lors de la validation du code postal
	            	            	    	    $(".erreur").hide();
	            	            	            $locexp.val(objet.placeName); //Met automatiquement la ville correspondante.  	
	            	            	            
	            	            	            if( !$locexp.val().match(/^[A-Za-zàáâãäåçèéêëìíîïðòóôõöùúûüýÿ\-]+$/i) ){
	            	            	            	var locexp = $locexp.val().substring($locexp.val().lastIndexOf(" "));
	            	            	            	$locexp.val(locexp); //remplace la ville par la sous-ville s'il y en a une.
	            	            	            }
	            	            				validExp = true;
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
	        			
	        			//on vérifie le champ code d'expéditeur au changement du champ	
	        			$codeexp.keyup(function(){
	            	       	verifierCodeExp($(this), $paysexp);
	            	    });
	        			//a chaque changement du code expediteur
	        			$codeexp.change(function(){
	            	       	verifierCodeExp($(this), $paysexp);
	            	    });
	        			
	        			var nameStartExp = $locexp.val();
	        			
	            	 	//complète automatiquement le champ (Ajax)
	            	 	$locexp.autocomplete({
	            	 		 source : function(requete, reponse){ // les deux arguments représentent les données nécessaires au plugin
	            	 			$.ajax({
	            	 		            url : 'http://api.geonames.org/searchJSON', // on appelle le script JSON
	            	 		            dataType : 'json', // on spécifie bien que le type de données est en JSON
	            	 		            data : {
	            	 		                name_startsWith : nameStartExp, //remplit la liste des villes en fonction du code postal tapé précédement ou non
	            	 		                
	            	 		                maxRows : 10,
	            	 		                country : 'BE',
	            	 		                username : 'sn1p3rx21x'
	            	 		            },
	            	 		            
	            	 		            success : function(donnee){
	            	 		                reponse($.map(donnee.geonames, function(objet){
	            	 		                	$locexp.css({ // on rend le champ vert
	            	            	    	        borderColor : 'green'
	            	            	    	    });
	            	 		                	$locexp.next(".error").fadeOut('slow');
	            	            				validExp = true;
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
	            	 	
	            	 	//a chaque changement de la ville expediteur
	            	 	$locexp.keyup(function(){
	            	       	verifier($(this));
	            	    });
	            	 	//a chaque changement de rue exp
	            	 	$rueexp.keyup(function(){
	            	       	verifierRue($(this));
	            	    });
	            	 	//a chaque changement du num exp
	            	 	$numexp.keyup(function(){
	            	       	verifierNum($(this));
	            	    });
	            	 	//a chaque changement de boite exp
	            	 	$boiteexp.keyup(function(){
	            	       	verifierBoite($(this));
	            	    });
	            	 	//a chaque changement de téléphone exp
	            	 	$telexp.keyup(function(){
	            	       	verifierTelExp($(this));
	            	    });
	            	 	
	            	 	
	            	 	
	            	 	
	            	 	/*DESTINATAIRE*/
	            	 	
	            	 	
	            	 	
	            	 	//a chaque changement du pays
	        			$paysdest.change(function(){
	            	       	if(verifier($(this))){
	            	       		$("input[name=adresse_code_destinataire]:text").show("slow", function(){}); //affiche le cp lors de la validation du pays	    	    
	            	       	} else {
	            	       		$("input[name=adresse_code_destinataire]:text").hide("slow", function(){}); //cache sinon	   
	            	       	}  	 
            	       		$("input[name=adresse_loc_destinataire]:text").hide("slow", function(){}); //cache la localite a chaque changement	  
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
	            	 	$codedest.autocomplete({
	            	 		 source : function(requete, reponse){ // les deux arguments représentent les données nécessaires au plugin
	            	 			$.ajax({
	            	 		            url : 'http://api.geonames.org/postalCodeSearchJSON', // on appelle le script JSON
	            	 		            dataType : 'json', // on spécifie bien que le type de données est en JSON
	            	 		            data : {
	            	 		            	postalcode_startsWith : $codedest.val(), // on donne la chaîne de caractère tapée dans le champ
	            	 		                maxRows : 1,
	            	 		                country : pays[$paysdest.val()], //recherche par pays
	            	 		                username : 'sn1p3rx21x'
	            	 		            },
	            	 		            
	            	 		            success : function(donnee){
	            	 		                reponse($.map(donnee.postalCodes, function(objet){
	            	 		                	$codeexp.css({ // on rend le champ vert
	            	            	    	        borderColor : 'green'
	            	            	    	    });
	            	 		                	$codedest.next(".error").fadeOut('slow');
	            	 		                	$locdest.next(".error").fadeIn('slow').text("Vérifier votre localité.");
	            	 		                	$("input[name=adresse_loc_destinataire]:text").show("slow", function(){}); //affiche la localité lors de la validation du code postal
	            	            	    	    $(".erreur").hide();
	            	            	            $locdest.val(objet.placeName); //Met automatiquement la ville correspondante.  	
	            	            	            
	            	            	            if( $paysdest.val() == "Belgique" && !$locdest.val().match(/^[A-Za-zàáâãäåçèéêëìíîïðòóôõöùúûüýÿ\-]+$/i) ){
	            	            	            	var locdest = $locdest.val().substring($locdest.val().lastIndexOf(" "));
	            	            	            	$locdest.val(locdest); //remplace la ville par la sous-ville s'il y en a une.
	            	            	            }
	            	            				validDest = true;
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
	        			
	        			//on vérifie le champ code du destinataire au changement du champ	
	        			$codedest.keyup(function(){
	            	       	verifierCodeDest($(this), $paysdest);
	            	    });
	        			//a chaque changement du code destinataire
	        			$codedest.change(function(){
	            	       	verifierCodeDest($(this), $paysdest);
	            	    });
	        			
	        			var nameStartDest = $locdest.val();
	        			
	            	 	//complète automatiquement le champ (Ajax)
	            	 	$locdest.autocomplete({
	            	 		 source : function(requete, reponse){ // les deux arguments représentent les données nécessaires au plugin
	            	 			$.ajax({
	            	 		            url : 'http://api.geonames.org/searchJSON', // on appelle le script JSON
	            	 		            dataType : 'json', // on spécifie bien que le type de données est en JSON
	            	 		            data : {
	            	 		                name_startsWith : nameStartDest, //remplit la liste des villes en fonction du code postal tapé précédement ou non	            	 		                
	            	 		                maxRows : 10,
	            	 		                country : pays[$paysdest.val()],  //recherche par pays
	            	 		                username : 'sn1p3rx21x'
	            	 		            },
	            	 		            
	            	 		            success : function(donnee){
	            	 		                reponse($.map(donnee.geonames, function(objet){
	            	 		                	$locdest.css({ // on rend le champ vert
	            	            	    	        borderColor : 'green'
	            	            	    	    });
	            	 		                	$locdest.next(".error").fadeOut('slow');
	            	            				validDest = true;
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
	            	 	
	            	 	//a chaque changement de la ville destinataire
	            	 	$locdest.keyup(function(){
	            	       	verifier($(this));
	            	    });
	            	 	//a chaque changement de rue dest
	            	 	$ruedest.keyup(function(){
	            	       	verifierRue($(this));
	            	    });
	            	 	//a chaque changement du num dest
	            	 	$numdest.keyup(function(){
	            	       	verifierNum($(this));
	            	    });
	            	 	//a chaque changement de boite dest
	            	 	$boitedest.keyup(function(){
	            	       	verifierBoite($(this));
	            	    });
	            	 	//a chaque changement de téléphone dest
	            	 	$teldest.keyup(function(){
	            	       	verifierTelDest($(this));
	            	    });

	            	 	
	            	 	
	            	 	/*COLIS*/
	            	 	
	            	 	
	            	 	
	            	 	
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
	            	        else if (!champ.val().match(/^[A-Za-z0-9àáâãäåçèéêëìíîïðòóôõöùúûüýÿ\-\ ]+$/i) ) {
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
							Fonction de vérification de formulaire du téléphone
						*/
	            	    function verifierTelExp(champ){
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
	            	    	    champ.next(".error").fadeIn().text(champ.val() + " n'est pas un format de téléphone valide.");
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
							Fonction de vérification de formulaire du téléphone spécifique au destinataire car il est facultatif
						*/
	            	    function verifierTelDest(champ){
	            	    	if(champ.val() == ""){ // si le champ est vide
	            	    		champ.css({ // on rend le champ vert
	            	    	        borderColor : 'green'
	            	    	    });
	            	            champ.next(".error").fadeOut('slow');
	            	            $(".erreur").hide();
	            	            return true;
	            	        }  //expression régulière pour matcher le format de téléphone internationnal
	            	    	else if ((!champ.val().match(/^(0|\+\d\d|00\d\d)([ -]?[1-9][0-9]{2})([-. /]?[0-9]{2})([-. ]?[0-9]{2}){2}$/i) ) ) {
	            	        	champ.css({ // on rend le champ rouge 
	            	    	        borderColor : 'red'
	            	    	    });
	            	    	    champ.next(".error").fadeIn().text(champ.val() + " n'est pas un format de téléphone valide.");
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
							Fonction de vérification de formulaire code postal pour l'expéditeur
						*/
	            	    function verifierCodeExp(champ, pays){
	            	        if(champ.val() == ""){ // si le champ est vide
	            	    	    champ.css({ // on rend le champ rouge
	            	    	        borderColor : 'red'
	            	    	    }); 
	            	        	$("input[name=adresse_loc_expediteur]:text").hide("slow", function(){});
	            	        	$locexp.next(".error").hide();
	 		                	nameStartExp = $locexp.val(); //pour faire remplir normalement la localité    	    	    
	            	    	    champ.next(".error").fadeIn().text("Veuillez entrer un code postal");
	            				$(".erreur").hide();
	            				return false;
	            	        } 
	            	        else if (!champ.val().match(/^[0-9]*$/i) ) { //si le champ ne contient pas que des chiffres.
	            	        	champ.css({ // on rend le champ rouge
	            	    	        borderColor : 'red'
	            	    	    });
	            	        	$("input[name=adresse_loc_expediteur]:text").hide("slow", function(){});
	            	        	$locexp.next(".error").hide();
	 		                	nameStartExp = $locexp.val(); //pour faire remplir normalement la localité   
	            	    	    champ.next(".error").fadeIn().text("Le code postal ne doit contenir que des chiffres.");
	            				$(".erreur").hide();
	            				return false;
	            	        }
	            	        else if (pays.val() == "Belgique" && (champ.val() < 1000  || champ.val() >= 10000) ) {
	            	        	champ.css({ // on rend le champ rouge
	            	    	        borderColor : 'red'
	            	    	    });
	            	        	$("input[name=adresse_loc_expediteur]:text").hide("slow", function(){});
	            	        	$locexp.next(".error").hide();
	 		                	nameStartExp = $locexp.val(); //pour faire remplir normalement la localité
	            	    	    champ.next(".error").fadeIn().text(champ.val() + " n'est pas un code postal valide en " + pays.val());
	            				$(".erreur").hide();
	            				return false;
	            	        }
	            	        else{
	            	        	champ.css({ // on rend le champ vert
	            	    	        borderColor : 'green'
	            	    	    });
	            	        	$("input[name=adresse_loc_expediteur]:text").show("slow", function(){});
	            	    	    nameStartExp = $codeexp.val(); //remplit la champ pour la recherche ajax avec le code pour automatiser la recherche
	            	            champ.next(".error").fadeOut('slow');
	            	            $(".erreur").hide();
	            	            return true;
	            	        }
	            	    }
	            	    
	            	    /*
							Fonction de vérification de formulaire code postal pour le destinataire
						*/
	            	    function verifierCodeDest(champ, pays){
	            	        if(champ.val() == ""){ // si le champ est vide
	            	    	    champ.css({ // on rend le champ rouge
	            	    	        borderColor : 'red'
	            	    	    }); 
	            	        	$("input[name=adresse_loc_destinataire]:text").hide("slow", function(){});
	            	        	$locdest.next(".error").hide();
	 		                	nameStartDest = $locdest.val(); //pour faire remplir normalement la localité    	    	    
	            	    	    champ.next(".error").fadeIn().text("Veuillez entrer un code postal");
	            				$(".erreur").hide();
	            				return false;
	            	        } 
	            	        else if (!champ.val().match(/^[A-Z0-9\ ]*$/i) ) { //si le champ est non valide.
	            	        	champ.css({ // on rend le champ rouge
	            	    	        borderColor : 'red'
	            	    	    });
	            	        	$("input[name=adresse_loc_destinataire]:text").hide("slow", function(){});
	            	        	$locdest.next(".error").hide();
	 		                	nameStartDest = $locdest.val(); //pour faire remplir normalement la localité   
	            	    	    champ.next(".error").fadeIn().text("Le code postal n'est pas valide.");
	            				$(".erreur").hide();
	            				return false;
	            	        }
	            	        else if (pays.val() == "Belgique" && (champ.val() < 1000  || champ.val() >= 10000) ) {
	            	        	champ.css({ // on rend le champ rouge
	            	    	        borderColor : 'red'
	            	    	    });
	            	        	$("input[name=adresse_loc_destinataire]:text").hide("slow", function(){});
	            	        	$locdest.next(".error").hide();
	 		                	nameStartDest = $locdest.val(); //pour faire remplir normalement la localité
	            	    	    champ.next(".error").fadeIn().text(champ.val() + " n'est pas un code postal valide en " + pays.val());
	            				$(".erreur").hide();
	            				return false;
	            	        }
	            	        else if ( champ.val() < 100  || champ.val() >= 100000 ) {
	            	        	champ.css({ // on rend le champ rouge
	            	    	        borderColor : 'red'
	            	    	    });
	            	        	$("input[name=adresse_loc_destinataire]:text").hide("slow", function(){});
	            	        	$locdest.next(".error").hide();
	 		                	nameStartDest = $locdest.val(); //pour faire remplir normalement la localité
	            	    	    champ.next(".error").fadeIn().text(champ.val() + " n'est pas un code postal valide pour ce pays");
	            				$(".erreur").hide();
	            				return false;
	            	        }
	            	        else{
	            	        	champ.css({ // on rend le champ vert
	            	    	        borderColor : 'green'
	            	    	    });
	            	        	$("input[name=adresse_loc_destinataire]:text").show("slow", function(){});
	            	    	    nameStartDest = $codedest.val(); //remplit la champ pour la recherche ajax avec le code pour automatiser la recherche
	            	            champ.next(".error").fadeOut('slow');
	            	            $(".erreur").hide();
	            	            return true;
	            	        }
	            	    }
	        });
	        
	        
	        
	        // Fait slider dynamiquement les différents blocs des formulaires.
	        // fonction jQuery permettant de cacher des parties de la commande au clic sur certains bouttons et d'en afficher d'autres.
            jQuery(document).ready(function(){
                /* 1 - Au lancement de la page, on cache les blocs d'éléments du formulaire  */
                $("div#noDefault").hide();
                $("div#noCarnetExp").hide();
                $("div#noCarnetDest").hide();
                $("div#Formule").hide();
                $("div#showAss").hide();
                //poursuite d'une étape à l'autre si il n'y a pas d'erreurs
                $("div#Colis").hide();
                $("div#adresses").show();
                //on cache les localités pour que le user choisissent d'abord le code postal. idem pays et cp
                $("input[name=adresse_loc_expediteur]:text").hide("slow", function(){});
                $("input[name=adresse_loc_destinataire]:text").hide("slow", function(){});
                $("input[name=adresse_code_destinataire]:text").hide("slow", function(){});
	    	    
                /* 2 - A la sélection du menu déroulant du carnet d'adresse, on affiche le bloc d'éléments correspondant */
                jQuery('select[name=CarnetExp]').click(function(){
                	if(document.getElementById("CarnetExp").value == "aucune"){
                		$("div#noCarnetExp").show("slow", function(){});
                		$("div#prec").hide("slow", function(){});
                		$("div#suiv").hide("slow", function(){});
                		var divId = jQuery(this).val();
	                    $("div#"+divId).show("slow", function(){});
                	}else if(document.getElementById("CarnetExp").value != ""){
                		$("div#noCarnetExp").show("slow", function(){});
                		$("div#prec").show("slow", function(){});
                		$("div#suiv").show("slow", function(){});
                		var divId = jQuery(this).val();
	                    $("div#"+divId).show("slow", function(){});
                	}else{
                		$("div#noCarnetExp").hide("slow", function(){});
                		$("div#prec").hide("slow", function(){});
                		$("div#suiv").hide("slow", function(){});
	                    var divId = jQuery(this).val();
	                    $("div#"+divId).show("slow", function(){});
                	}
                });
                
                jQuery('select[name=CarnetDest]').click(function(){
                	if(document.getElementById("CarnetDest").value == "aucune"){
                		$("div#noCarnetDest").show("slow", function(){});
                		$("div#precDest").hide("slow", function(){});
                		$("div#suivDest").hide("slow", function(){});
                		var divId = jQuery(this).val();
	                    $("div#"+divId).show("slow", function(){});
                	}else if(document.getElementById("CarnetDest").value != ""){
                		$("div#noCarnetDest").show("slow", function(){});
                		$("div#precDest").show("slow", function(){});
                		$("div#suivDest").show("slow", function(){});
                		var divId = jQuery(this).val();
	                    $("div#"+divId).show("slow", function(){});
                	}else{
                		$("div#noCarnetDest").hide("slow", function(){});
                		$("div#precDest").hide("slow", function(){});
                		$("div#suivDest").hide("slow", function(){});
	                    var divId = jQuery(this).val();
	                    $("div#"+divId).show("slow", function(){});
                	}
                });
                /* Au clic sur le bouton radio non pour l'adresse defaut de l'expéditeur, on affiche le menu déroulant du carnet d'adresses */
                jQuery('input[name=AdresseDefaut]:radio').click(function(){
                    $("div#noDefault").hide("slow", function(){});
                    var divId = jQuery(this).val();
                    $("div#"+divId).show("slow", function(){});
                });
                
                /* On affiche les informations sur les adresses et cache celles sur le colis. */
                jQuery('input[name=modifAdr]:button').click(function(){
                    $("div#Colis").hide("slow", function(){});
                    $("div#adresses").show("slow", function(){});
                    var divId = jQuery(this).val();
                    $("div#"+divId).show();
                });
                
            });