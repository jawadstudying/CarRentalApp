<?php

namespace App\Http\Controllers;

use App\Models\Car;
use App\Models\RequestEntry;
use Illuminate\Http\Request;

class OnlineRequestController extends Controller
{
    // Show page with available cars + request form
    public function index()
    {
        // Only cars that are AVAILABLE
        $cars = Car::where('status', 'AVAILABLE')->orderBy('car_id')->get();

        return view('online_requests', [
            'cars' => $cars,
        ]);
    }

    // Handle request form submission
    public function store(Request $request)
    {
        $data = $request->validate([
            'first_name' => ['required', 'string', 'max:50'],
            'last_name'  => ['required', 'string', 'max:50'],
            'phone'      => ['required', 'string', 'max:20'],
            'email'      => ['required', 'email', 'max:100'],
            'car_id'     => ['required', 'integer'],
        ]);

        RequestEntry::create($data);

        return redirect()
            ->back()
            ->with('success', 'Thank you! Your request has been submitted. We may call you to confirm the request.');
    }
}
