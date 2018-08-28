        $.ajax({
            url: "/persons",
            success: function(data){
                console.log("persons:", data);
                $.each(data, function(i, e){
                     $('#persons').append('<li>' + e.id + ' --- ' + e.name + '</li>');
                 });
            },
            error: function(data){
                alert("error!!! " + data);
            }
        });
