<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Rent-A-Car - Online Request</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    {{-- Bootstrap 5 CDN --}}
    <link
        href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css"
        rel="stylesheet"
    >

    <style>
        body {
            background: #f5f5f5;
        }
        .page-wrapper {
            min-height: 100vh;
            display: flex;
            align-items: center;
            justify-content: center;
            padding: 30px 15px;
        }
        .card-main {
            max-width: 1100px;
            width: 100%;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.08);
            border-radius: 16px;
            overflow: hidden;
        }
        .brand-title {
            font-weight: 700;
            letter-spacing: 0.08em;
            font-size: 0.9rem;
            text-transform: uppercase;
            color: #6c757d;
        }
        .hero-title {
            font-size: 1.6rem;
            font-weight: 700;
        }
        .small-text {
            font-size: 0.9rem;
        }
        .car-table {
            max-height: 360px;
            overflow-y: auto;
        }
        .badge-available {
            background-color: #198754;
        }
    </style>
</head>
<body>
<div class="page-wrapper">
    <div class="card card-main bg-white border-0">
        <div class="row g-0">
            {{-- Left side: cars list --}}
            <div class="col-lg-6 border-end">
                <div class="p-4 p-lg-5">
                    <div class="mb-2 brand-title">
                        Rent-A-Car
                    </div>
                    <h1 class="hero-title mb-2">
                        Choose your car and send a request
                    </h1>
                    <p class="text-muted small-text mb-4">
                        Browse our currently available cars. When you submit a request, our team may call you
                        to confirm the details and complete your booking.
                    </p>

                    {{-- Success / Error messages --}}
                    @if(session('success'))
                        <div class="alert alert-success py-2 small-text">
                            {{ session('success') }}
                        </div>
                    @endif

                    @if($errors->any())
                        <div class="alert alert-danger py-2 small-text">
                            <ul class="mb-0">
                                @foreach($errors->all() as $error)
                                    <li>{{ $error }}</li>
                                @endforeach
                            </ul>
                        </div>
                    @endif

                    <h5 class="mb-3">Available cars</h5>
                    <div class="car-table border rounded-3 bg-light">
                        <table class="table table-sm table-hover mb-0">
                            <thead class="table-light" style="position: sticky; top: 0; z-index: 1;">
                            <tr>
                                <th>ID</th>
                                <th>Plate</th>
                                <th>Brand</th>
                                <th>Model</th>
                                <th>Rate / day</th>
                            </tr>
                            </thead>
                            <tbody>
                            @forelse($cars as $car)
                                <tr>
                                    <td>{{ $car->car_id }}</td>
                                    <td>{{ $car->license_plate }}</td>
                                    <td>{{ $car->brand }}</td>
                                    <td>{{ $car->model }}</td>
                                    <td>
                                        @if($car->daily_rate !== null)
                                            ${{ number_format($car->daily_rate, 2) }}
                                        @else
                                            -
                                        @endif
                                    </td>
                                </tr>
                            @empty
                                <tr>
                                    <td colspan="5" class="text-center text-muted">
                                        No cars are currently available.
                                    </td>
                                </tr>
                            @endforelse
                            </tbody>
                        </table>
                    </div>

                    <p class="text-muted small-text mt-3 mb-0">
                        Tip: Note the <strong>Car ID</strong> you want to request. You will need it in the form.
                    </p>
                </div>
            </div>

            {{-- Right side: request form --}}
            <div class="col-lg-6">
                <div class="p-4 p-lg-5">
                    <h4 class="mb-3">Request this car</h4>
                    <p class="small-text text-muted mb-4">
                        Fill in your contact details and the ID of the car you want.
                        We may call you to confirm the request and finalize the rental.
                    </p>

                    <form method="POST" action="{{ route('online-requests.store') }}" class="row g-3">
                        @csrf

                        <div class="col-md-6">
                            <label class="form-label small-text">First name</label>
                            <input
                                type="text"
                                name="first_name"
                                class="form-control form-control-sm"
                                value="{{ old('first_name') }}"
                                required
                            >
                        </div>

                        <div class="col-md-6">
                            <label class="form-label small-text">Last name</label>
                            <input
                                type="text"
                                name="last_name"
                                class="form-control form-control-sm"
                                value="{{ old('last_name') }}"
                                required
                            >
                        </div>

                        <div class="col-md-6">
                            <label class="form-label small-text">Phone</label>
                            <input
                                type="text"
                                name="phone"
                                class="form-control form-control-sm"
                                value="{{ old('phone') }}"
                                required
                            >
                        </div>

                        <div class="col-md-6">
                            <label class="form-label small-text">Email</label>
                            <input
                                type="email"
                                name="email"
                                class="form-control form-control-sm"
                                value="{{ old('email') }}"
                                required
                            >
                        </div>

                        <div class="col-md-6">
                            <label class="form-label small-text">Car ID</label>
                            <input
                                type="number"
                                name="car_id"
                                class="form-control form-control-sm"
                                value="{{ old('car_id') }}"
                                required
                                min="1"
                            >
                            <div class="form-text small-text">
                                Enter the <strong>Car ID</strong> from the list on the left.
                            </div>
                        </div>

                        <div class="col-12 mt-3">
                            <button type="submit" class="btn btn-primary w-100">
                                Send Request
                            </button>
                        </div>

                        <div class="col-12">
                            <p class="small-text text-muted mb-0 mt-2">
                                By submitting this form, you agree that we may contact you by phone or email
                                to confirm your request and complete the reservation.
                            </p>
                        </div>
                    </form>

                </div>
            </div>
        </div>
    </div>
</div>

<script
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js">
</script>
</body>
</html>
