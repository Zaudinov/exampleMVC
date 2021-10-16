<#include "security.ftl">
<div class="card-columns">
    <#list messages as message>
        <div class="card my-3">
            <#if message.filename??>
                <img src="/img/${message.filename}" class="card-img-top">
            </#if>
            <div class="m-2">
                <span>${message.text}</span><br/>
                <i>#${message.tag}</i>
            </div>
            <div class="card-footer text-muted">
                <a href="/user-messages/${message.author.id}">${message.authorName}</a>
                <#if message.author.id == currentUserId>
                    <a href="/user-messages/${message.author.id}?message=${message.id}"
                    class="btn btn-primary">
                        Edit
                    </a>
                    <form method="POST" action="/user-messages/${message.author.id}?message=${message.id}">
                        <input type="hidden" name="_method" value="DELETE"/>
                        <input type="hidden" name="_csrf" value="${_csrf.token}" />
                        <input type="submit" class="btn btn-danger" value="Delete"/>
                    </form>
                </#if>
            </div>
        </div>
        <#else>
            No message
    </#list>
</div>