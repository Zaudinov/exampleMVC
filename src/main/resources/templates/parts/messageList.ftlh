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
                <a class="m-1" href="/user-messages/${message.author.id}">${message.authorName}</a>
                <#if message.author.id == currentUserId>
                    <a style="display:inline;" href="/user-messages/${message.author.id}?message=${message.id}"
                       class="btn btn-primary">
                        Edit
                    </a>

                    <form style="display:inline;" class="m-1" method="POST" action="/user-messages/${message.author.id}?message=${message.id}">
                        <input type="hidden" name="_method" value="DELETE"/>
                        <input type="hidden" name="_csrf" value="${_csrf.token}" />
                        <button type="submit" class="btn btn-danger">Delete</button>
                    </form>
                </#if>
            </div>
        </div>
        <#else>
            No message
    </#list>
</div>