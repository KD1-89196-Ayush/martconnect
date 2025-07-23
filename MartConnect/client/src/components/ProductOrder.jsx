import React, { useState } from "react";
import { useNavigate } from "react-router-dom";

const Orders = () => {
    const navigate = useNavigate();

    const dummyOrders = [
        {
            id: 1,
            customerName: "Rahul Patil",
            total: 400,
            isPaid: false,
            status: "Pending",
            date: "2025-07-20",
            orderItems: [
                { name: "Wheat", quantity: 4, unit: "Kg" },
                { name: "Lux Soap", quantity: 4, unit: "" },
            ],
        },
        {
            id: 2,
            customerName: "Pradeep Patil",
            total: 280,
            isPaid: true,
            status: "Delivered",
            date: "2025-07-22",
            orderItems: [
                { name: "Sugar", quantity: 2, unit: "Kg" },
                { name: "Toothpaste", quantity: 1, unit: "" },
            ],
        },

        {
            id: 3,
            customerName: "Amit Patil",
            total: 580,
            isPaid: true,
            status: "Delivered",
            date: "2025-07-21",
            orderItems: [
                { name: "Sugar", quantity: 2, unit: "Kg" },
                { name: "Toothpaste", quantity: 1, unit: "" },
            ],
        },

        {
            id: 4,
            customerName: "Raj Patil",
            total: 580,
            isPaid: true,
            status: "Delivered",
            date: "2025-07-28",
            orderItems: [
                { name: "Sugar", quantity: 2, unit: "Kg" },
                { name: "Toothpaste", quantity: 1, unit: "" },
            ],
        },


        {
            id: 5,
            customerName: "Tanu Patil",
            total: 580,
            isPaid: true,
            status: "Delivered",
            date: "2025-07-19",
            orderItems: [
                { name: "Sugar", quantity: 2, unit: "Kg" },
                { name: "Toothpaste", quantity: 1, unit: "" },
            ],
        },


        {
            id: 6,
            customerName: "Shubh Patil",
            total: 580,
            isPaid: true,
            status: "Delivered",
            date: "2025-07-29",
            orderItems: [
                { name: "Sugar", quantity: 2, unit: "Kg" },
                { name: "Toothpaste", quantity: 1, unit: "" },
            ],
        },


        {
            id: 7,
            customerName: "Raj Patil",
            total: 580,
            isPaid: false,
            status: "Delivered",
            date: "2025-07-02",
            orderItems: [
                { name: "Sugar", quantity: 2, unit: "Kg" },
                { name: "Toothpaste", quantity: 1, unit: "" },
            ],
        },
    ];

    const [searchText, setSearchText] = useState("");
    const [filteredOrders, setFilteredOrders] = useState(dummyOrders);

    const handleSearch = () => {
        const filtered = dummyOrders.filter((order) =>
            order.customerName.toLowerCase().includes(searchText.toLowerCase())
        );
        setFilteredOrders(filtered);
    };

    // Sort orders based on most recent date
    const handleRecentOrders = () => {
        const sorted = [...dummyOrders].sort((a, b) => new Date(b.date) - new Date(a.date));
        setFilteredOrders(sorted);
    };

    // Filter only pending orders
    const handlePendingOrders = () => {
        const filtered = dummyOrders.filter((order) => order.status.toLowerCase() === "pending");
        setFilteredOrders(filtered);
    };

    // Filter only paid orders
    const handlePaidOrders = () => {
        const filtered = dummyOrders.filter((order) => order.isPaid === true);
        setFilteredOrders(filtered);
    };


    const handleOrderDetails = (order) => {
        navigate("/orderDetails", { state: { order } });
    };

    return (
        <div className="container mt-1" style={{ maxWidth: "1000px", marginBottom:"100px", marginLeft: "40vh"}}>
            <h3 className="text-center mb-10" >Orders</h3>

            {/* Search bar */}
            {/* Search bar and filter buttons */}
            <div className="mb-3 d-flex gap-2" >
                <input
                    type="text"
                    placeholder="Search by customer name..."
                    className="form-control"
                    value={searchText}
                    onChange={(e) => setSearchText(e.target.value)}
                />
                <button className="btn btn-primary" onClick={handleSearch}>Search</button>
                <button className="btn btn-success" onClick={handleRecentOrders}>Recent </button>
                <button className="btn btn-warning" onClick={handlePendingOrders}>Pending </button>
                <button className="btn btn-info" onClick={handlePaidOrders}>Paid</button>
            </div>



            {/* Orders Table */}
            <table className="table table-bordered">
                <thead className="table-dark">
                    <tr>
                        <th>Sr.No</th>
                        <th>Customer Name</th>
                        <th>Order Details</th>
                        <th>Total Bill</th>
                        <th>Payment Status</th>
                        <th>Order Status</th>
                        <th>Order Date</th>
                    </tr>
                </thead>
                <tbody>
                    {filteredOrders.map((order, index) => (
                        <tr key={order.id}>
                            <td>{index + 1}</td>
                            <td>{order.customerName}</td>
                            <td>
                                <button
                                    className="btn btn-outline-info btn-sm"
                                    onClick={() => handleOrderDetails(order)}
                                >
                                    Order List
                                </button>
                            </td>
                            <td>₹{order.total}</td>
                            <td>
                                <input
                                    type="checkbox"
                                    checked={order.isPaid}
                                    readOnly
                                />{" "}
                                {order.isPaid ? "Paid" : "Pending"}
                            </td>
                            <td>{order.status}</td>
                            <td>{order.date}</td>
                        </tr>
                    ))}
                </tbody>

            </table>
        </div>
    );
};

export default Orders;
