paymentButton.addEventListener("click", async function () {
    const amount = parseInt(document.getElementById("amount").value);
    const orderName = document.getElementById("orderName").value;
    const customerEmail = document.getElementById("customerEmail").value;
    const orderId = crypto.randomUUID(); // UUID 자동 생성

    // 🟡 [1] 백엔드에 먼저 주문 생성 요청
    try {
        const res = await fetch("http://localhost:8080/api/order/create", {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
                "Authorization": "Bearer " + localStorage.getItem("token") // 필요 시
            },
            body: JSON.stringify({
                orderId: orderId,
                orderName: orderName,
                amount: amount,
                // 추가로 storeId, requestMessage 등 필요한 값도 같이 보낼 수 있음
            }),
        });

        if (!res.ok) throw new Error("주문 생성 실패");

        console.log("🟢 백엔드에 주문 저장 성공:", orderId);
    } catch (error) {
        alert("주문 생성 중 오류 발생: " + error.message);
        return;
    }

    // 🟢 [2] Toss 결제창 띄우기
    tossPayments
        .requestPayment("CARD", {
            amount: amount,
            orderId: orderId,
            orderName: orderName,
            customerName: "홍길동",
            customerEmail: customerEmail,
            successUrl: `http://localhost:8080/api/payment/success?orderId=${orderId}&amount=${amount}`,
            failUrl: "http://localhost:8080/api/payment/fail",
        })
        .catch(function (error) {
            alert("결제 요청 중 오류 발생: " + error.message);
        });
});
