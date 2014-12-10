var express = require('express');
var router = express.Router();


router.post('/create', function(req, res) {

	var product_name = req.body.product_name;
	var taste = req.body.taste;
	var packaging = req.body.taste;


	// Set our internal DB variable
	var db = req.db;

	// Set our collection
	var collection = db.get("reviews");

	collection.insert({
		"product_name" : product_name,
		"taste" : taste,
		"packaging" : packaging
	}, function (err, doc) {
		
		if (err) {
			res.send("There was a problem adding the information to the database.");
		}

		else {          
			res.send('Added review!');
		}
	
	});
});

router.post('/create/2', function(req, res) {

	var product_name = req.body.product_name;
	var smell = req.body.smell;
	var comments = req.body.comments;


	// Set our internal DB variable
	var db = req.db;

	// Set our collection
	var collection = db.get("reviews");

	collection.update({ "product_name" : product_name },
		{ $set: { 
			"smell" : smell ,
			"comments" : comments
		}
	});
	
	res.send("Added review!")
});

router.post('/view', function(req, res) {

	var product_name = req.body.product_name;

	console.log(product_name);


	// Set our internal DB variable
	var db = req.db;

	// Set our collection
	var collection = db.get("reviews");

	collection.find( { "product_name" : product_name }, {}, function (err, doc) {
		res.send(doc);
	 });

});

module.exports = router;
