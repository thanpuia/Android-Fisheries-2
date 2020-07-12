//package com.give.android_fisheries_2.admin;
//
//import android.os.Bundle;
//
//import androidx.fragment.app.Fragment;
//
//import android.util.Log;
//import android.view.InflateException;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import com.give.android_fisheries_2.R;
//import com.google.android.gms.maps.GoogleMap;
//import com.google.android.gms.maps.MapFragment;
//import com.google.android.gms.maps.OnMapReadyCallback;
//import com.google.android.gms.maps.model.LatLng;
//import com.google.android.gms.maps.model.MarkerOptions;
//
///**
// * A simple {@link Fragment} subclass.
// */
//public class PondsFragment extends Fragment implements OnMapReadyCallback {
//
//    public PondsFragment() {
//        // Required empty public constructor
//    }
//
//
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        View view =null;//= inflater.inflate(R.layout.fragment_ponds, container, false);
//
//        if (view != null) {
//            ViewGroup parent = (ViewGroup) view.getParent();
//            if (parent != null)
//                parent.removeView(view);
//        }
//        try {
//            view = inflater.inflate(R.layout.fragment_ponds, container, false);
//        } catch (InflateException e) {
//
//            Log.d("TAG","CARCH: "+e);
//
//           // return inflater.inflate(R.layout.fragment_ponds, container, false);
//                 MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
//                            .findFragmentById(R.id.maps);
//                    if(mapFragment==null)
//                        mapFragment.getMapAsync(this);
//
//
//        }
//
//
//        MapFragment mapFragment = (MapFragment) getActivity().getFragmentManager()
//                .findFragmentById(R.id.maps);
//        if(mapFragment==null)
//            mapFragment.getMapAsync(this);
//
//
//        return view;
//    }
//
//    @Override
//    public void onMapReady(GoogleMap googleMap) {
//        googleMap.addMarker(new MarkerOptions()
//                .position(new LatLng(0, 0))
//                .title("Marker"));
//
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//
//    }
//
//    @Override
//    public void onPause() {
//        super.onPause();
//
//        android.app.Fragment mapFragment = getActivity().
//                        getFragmentManager().findFragmentById(R.id.maps);
//
//        if(mapFragment!=null){
//            getActivity().getFragmentManager().beginTransaction().remove(mapFragment).commit();
//        }
//
//    }
//}
