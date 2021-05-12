<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<tags:master pageTitle="Product Details" cart="${cart}">
    <a href="${pageContext.servletContext.contextPath}/productList" class="btn btn-dark btn-lg">
        Back to product list
    </a>
    <h2 style="margin-top: 20px">${phone.model}</h2>
    <div class="row">
        <div class="col">
            <img src="https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/${phone.imageUrl}">
            <div style="margin-top: 20px">${phone.description}</div>
            <div class="card" style="width: 18rem; margin-top: 20px">
                <div class="card-body">
                    <h5 class="card-title">Price: ${phone.price}$</h5>
                    <div class="input-group mb-3">
                        <input id="input${phone.id}" class="form-control" value="1">
                        <div class="input-group-append">
                            <button id="${phone.id}" class="btn btn-outline-dark">Add to cart</button>
                        </div>
                        <div id="message${phone.id}" style="margin-top: 10px; width: 18rem;"></div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col">
            <h3>Display</h3>
            <table class="table table-striped table-bordered">
                <tbody>
                <tr>
                    <td style="width: 200px">Size</td>
                    <td style="width: 200px">${phone.displaySizeInches}''</td>
                </tr>
                <tr>
                    <td>Resolution</td>
                    <td>${phone.displayResolution}</td>
                </tr>
                <tr>
                    <td>Technology</td>
                    <td>${phone.displayTechnology}</td>
                </tr>
                <tr>
                    <td>Pixel density</td>
                    <td>${phone.pixelDensity}</td>
                </tr>
                </tbody>
            </table>
            <h3>Dimensions & weight</h3>
            <table class="table table-striped table-bordered">
                <tbody>
                <tr>
                    <td style="width: 200px">Length</td>
                    <td style="width: 200px">${phone.lengthMm} mm</td>
                </tr>
                <tr>
                    <td>Width</td>
                    <td>${phone.widthMm} mm</td>
                </tr>
                <tr>
                    <td>Height</td>
                    <td>${phone.heightMm} mm</td>
                </tr>
                <tr>
                    <td>Weight</td>
                    <td>${phone.weightGr} gr</td>
                </tr>
                </tbody>
            </table>
            <h3>Camera</h3>
            <table class="table table-striped table-bordered">
                <tbody>
                <tr>
                    <td style="width: 200px">Front</td>
                    <td style="width: 200px">${phone.frontCameraMegapixels} megapixels</td>
                </tr>
                <tr>
                    <td>Back</td>
                    <td>${phone.backCameraMegapixels} megapixels</td>
                </tr>
                </tbody>
            </table>
            <h3>Battery</h3>
            <table class="table table-striped table-bordered">
                <tbody>
                <tr>
                    <td style="width: 200px">Talk time</td>
                    <td style="width: 200px">${phone.talkTimeHours} hours</td>
                </tr>
                <tr>
                    <td>Stand by time</td>
                    <td>${phone.standByTimeHours} hours</td>
                </tr>
                <tr>
                    <td>Battery capacity</td>
                    <td>${phone.batteryCapacityMah} mAh</td>
                </tr>
                </tbody>
            </table>
            <h3>Other</h3>
            <table class="table table-striped table-bordered">
                <tbody>
                <tr>
                    <td style="width: 200px">Device type</td>
                    <td style="width: 200px">${phone.deviceType} </td>
                </tr>
                <tr>
                    <td>Bluetooth</td>
                    <td>${phone.bluetooth}</td>
                </tr>
                <tr>
                    <td>Colors</td>
                    <td>
                        <c:forEach var="color" items="${phone.colors}">
                            ${color.code}<br>
                        </c:forEach>
                    </td>
                </tr>
                </tbody>
            </table>
        </div>
    </div>
</tags:master>