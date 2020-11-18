$(document).ready(function ()
{
    $('a[data-toggle="tab"]').on('shown.bs.tab', function (e)
    {
        refreshTabContent($(e.target).attr('name'));
    });

    $('[data-toggle="tooltip"]').tooltip();
});

function getHeaders()
{
    var csrfTokenValue = $("meta[name='_csrf']").attr("content");
    var csrfHeaderName = $("meta[name='_csrf_header']").attr("content");

    var headerValue = [];
    headerValue[csrfHeaderName] = csrfTokenValue;

    return headerValue;
}

function showAlertSuccessMsg(message)
{
    var danger = alertMessage.find('.alert-danger');

    if ($(danger).hasClass('show'))
    {
        $(danger).removeClass('show');
        $(danger).addClass('hide');
    }

    var msg = alertMessage.find('.alert-success');
    $(msg).html(message);
    $(msg).removeClass('hide');
    $(msg).addClass('show');
}

function showAlertDangerMsg(error, message)
{
    var success = alertMessage.find('.alert-success');

    if ($(success).hasClass('show'))
    {
        $(success).removeClass('show');
        $(success).addClass('hide');
    }

    var msg = alertMessage.find('.alert-danger');
    $(msg).html(error + '<br>' + message);
    $(msg).removeClass('hide');
    $(msg).addClass('show');
}

function saveRecord()
{
    $.ajax(
        {
            url: urlSaveRecord,
            type: 'POST',
            headers: getHeaders(),
            data: recordForm.serialize(),
            success: function ()
            {
                console.log("Record Saved");
                showAlertSuccessMsg("Registro guardado correctamente.");

                $(recordForm).each(function()
                {
                    this.reset();
                });
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
                console.error('Failed to save record. Error: ' + jqXHR.status + ' ' + jqXHR.responseText);
                showAlertDangerMsg(jqXHR.status, jqXHR.responseText);
            }
        });
}

function addExpose()
{
    $.ajax(
        {
            url: urlAddExpose,
            type: 'POST',
            headers: getHeaders(),
            data: addExposeForm.serialize(),
            success: function ()
            {
                console.log('Added Exposed.');
                showAlertSuccessMsg("Registro reportado positivo.");
                console.log('Reloading Records Tab.');
                recordContentId.load(urlRefreshRecords);
            },
            error: function(jqXHR, textStatus, errorThrown)
            {
                console.error('Failed to add exposed. Error: ' + jqXHR.status + ' ' + jqXHR.responseText);
                showAlertDangerMsg(jqXHR.status, jqXHR.responseText);
            }
        });
}

function deleteRecord(recordId)
{
    $.ajax(
        {
            url: urlDeleteRecord,
            type: 'POST',
            headers: getHeaders(),
            dataType: 'json',
            data: {
                'recordId': recordId
            },
            complete: function (e, xhr, settings)
            {
                if (e.status === 200)
                {
                    console.log('Record Deleted.');
                    showAlertSuccessMsg("Registro eliminado.");
                    recordContentId.load(urlRefreshRecords);
                }
                else
                {
                    console.error('Failed to delete record. Error: ' + e);
                    showAlertDangerMsg(e.status, e.responseText);
                }
            }
        });
}

function newCovidCode()
{
    let covidCode = parent.document.getElementById("covid-code-id");

    $.ajax(
        {
            url: urlNewCode,
            type: 'GET',
            headers: getHeaders(),
            success: function (data, textStatus, jqXHR)
            {
                covidCode.innerHTML = data;
            },
            error: function (jqXHR, textStatus, errorThrown)
            {
            },
            complete: function (e, xhr, settings)
            {
                if (e.status === 200)
                {
                    console.log('Código covid generado.');
                }
                else
                {
                    console.error('Falló al generar el código. Error: ' + e);
                }
            }
        });
}

function hideMessages()
{
    var successMsg = alertMessage.find('.alert-success');
    var dangerMsg = alertMessage.find('.alert-danger');
    $(successMsg).removeClass('show');
    $(successMsg).addClass('hide');
    $(dangerMsg).removeClass('show');
    $(dangerMsg).addClass('hide');
}

function refreshTabContent(id)
{
    hideMessages();

    if (id === "records")
    {
        //recordContentId.load(urlRefreshRecords);
    }
    else if (id === "historic")
    {
        historicContent.load(urlRefreshHistoric);
    }else if (id === "news")
    {
    	newsContent.load(urlRefreshNews);
    }else if (id === "questions")
    {
    	questionsContent.load(urlRefreshQuestions);
    }
}

