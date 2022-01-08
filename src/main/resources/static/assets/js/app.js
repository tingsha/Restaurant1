$(function(){

    let headerHeight = $("#header").innerHeight();
    let headerLine = $("#header__line");
    let scrollOffset = $(window).scrollTop();


    /* Fixed Header */
    checkScroll(scrollOffset);

    $(window).on("scroll", function(){
        scrollOffset = $(this).scrollTop();

        checkScroll(scrollOffset);
    });

    function checkScroll(scrollOffset){


        if (scrollOffset >= headerHeight){
            headerLine.addClass("fixed");
        } else{
            headerLine.removeClass("fixed");
        }
    }

    /* Smooth scroll */
    $("[data-scroll]").on("click", function(event){
        event.preventDefault();

        let $this = $(this);
        let sectionId = $(this).data('scroll');
        let sectionOffset = $(sectionId).offset().top;

        $("[data-scroll]").removeClass("active");
        $("#burger").toggleClass("active");
        $this.addClass("active");

        $("html, body").animate({
            scrollTop: sectionOffset+1
        }, 300);
    })

    /* Burger toggle */
    $("#burger").on("click", function(event){
        event.preventDefault();

        $("#burger").toggleClass("active");
        $("#header__nav").toggleClass("active");
    });

    /* Active section */
    $(window).on("scroll", function(){
        scrollOffset = $(this).scrollTop();

        $("[data-scroll]").removeClass("active");
        if (scrollOffset > $("#reservations").offset().top){
            $("#reservations__link").addClass("active");
        }
        else if (scrollOffset > $("#reviews").offset().top){
            $("#reviews__link").addClass("active");
        }
        else if (scrollOffset > $("#menu").offset().top){
            $("#menu__link").addClass("active");
        }
        else if (scrollOffset > $("#ingredients").offset().top){
            $("#ingredients__link").addClass("active");
        }
        else if (scrollOffset > $("#about").offset().top){
            $("#about__link").addClass("active");
        }
        else if (scrollOffset > $("#promo").offset().top){
            $("#promo__link").addClass("active");
        }
    });

    /* Prevent default post method */

    $('#addBooking').submit(function (event) {
        event.preventDefault();

        let booking = {
            "name": $("#name").val(),
            "email": $("#email").val(),
            "date": $("#calendar").val(),
            "partyNumber": $("#party").val()
        }

        $.ajax({
            url: "/",
            data: JSON.stringify(booking),
            type: "POST",
            dataType: 'json',
            contentType: 'application/json',

            beforeSend: function (xhr) {
                xhr.setRequestHeader("Accept", "application/json");
                xhr.setRequestHeader("Content-Type", "application/json");
            },

            success: function (data) {
                let messages = ["", "", "", "", ""];

                if (!data.isNameValid){
                    messages[0] = "Name can\'t be empty";
                }

                if (!data.isEmailValid){
                    messages[1] = "Invalid email";
                }

                if (!data.isPartyNumberValid){
                    messages[2] = "Select party number";
                }

                if (!data.isTimeValid){
                    messages[3] = "Opening hours: " +
                        "Mon-Thu: 7:00am-8:00pm " +
                        "Fri-Sun: 7:00am-10:00pm"
                }

                if (!data.notSpam){
                    messages[4] = "You can book only one table"
                }

                console.log(messages);

                $.each(messages, function (index, value) {
                    if (value !== "")
                        new bs5.Toast({
                            body: value,
                            className: 'border-0 bg-danger text-white',
                            btnCloseWhite: true,
                            placement: 'bottom-right',
                            delay: 3000
                        }).show();
                })

                let isAllFieldsValid = true;
                $.each(messages, function (index, value) {
                    if (value !== "")
                        isAllFieldsValid = false;
                })

                if (isAllFieldsValid) {
                    new bs5.Toast({
                        body: 'Wait for the confirmation',
                        className: 'border-0 bg-success text-white',
                        btnCloseWhite: true,
                        placement: 'bottom-right',
                        delay: 3000
                    }).show();
                    window.open(
                        '/bookings',
                        '_blank'
                    );
                }
            },
        });
    });
});