package com.quyetdw.winmall.service.impl;

import com.quyetdw.winmall.config.JwtProvider;
import com.quyetdw.winmall.domain.AccountStatus;
import com.quyetdw.winmall.exception.SellerException;
import com.quyetdw.winmall.model.Address;
import com.quyetdw.winmall.model.Seller;
import com.quyetdw.winmall.repository.AddressRepository;
import com.quyetdw.winmall.repository.SellerRepository;
import com.quyetdw.winmall.service.SellerService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SellerServiceImpl implements SellerService {

    private final SellerRepository sellerRepository;
    private final JwtProvider jwtProvider;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Seller getSellerProfile(String jwt) throws Exception {

        String email = jwtProvider.getEmailFromJwtToken(jwt);

        return this.getSellerByEmail(email);
    }

    @Override
    public Seller createSeller(Seller seller) throws Exception {
        Seller sellerExited = sellerRepository.findByEmail(seller.getEmail());
        if (sellerExited != null){
            throw new Exception("Email này đã tồn tại!");
        }
        Address saveAddress = addressRepository.save(seller.getPickupAddress());

        Seller newSeller = new Seller();
        newSeller.setEmail(seller.getEmail());
        newSeller.setSellerName(seller.getSellerName());
        newSeller.setEmail(seller.getEmail());
        newSeller.setPassword(passwordEncoder.encode(seller.getPassword()));
        newSeller.setPickupAddress(seller.getPickupAddress());
        newSeller.setGSTIN(seller.getGSTIN());
        newSeller.setMobile(seller.getMobile());
        newSeller.setRole(seller.getRole());
        newSeller.setBankDetails(seller.getBankDetails());
        newSeller.setBusinessDetails(seller.getBusinessDetails());

        return sellerRepository.save(newSeller);
    }

    @Override
    public Seller getSellerById(Long id) throws SellerException {

        return sellerRepository.findById(id)
                .orElseThrow(() -> new SellerException("Không tìm thấy người dùng!"));
    }

    @Override
    public Seller getSellerByEmail(String email) throws Exception {
        Seller seller = sellerRepository.findByEmail(email);
        if (seller == null){
            throw new Exception("Không thể tìm thấy Seller!");
        }

        return seller;
    }

    @Override
    public List<Seller> getAllSellers(AccountStatus status) {
        return sellerRepository.findByAccountStatus(status);
    }

    @Override
    public Seller updateSeller(Long id, Seller seller) throws Exception {

        Seller exitingSeller = this.getSellerById(id);

        if (seller.getSellerName() != null){
            exitingSeller.setSellerName(seller.getSellerName());
        }
        if (seller.getMobile() != null){
            exitingSeller.setMobile(seller.getMobile());
        }
        if (seller.getEmail() != null){
            exitingSeller.setEmail(seller.getEmail());
        }
        if (seller.getBusinessDetails() != null && seller.getBusinessDetails().getBusinessName() != null){
            exitingSeller.getBusinessDetails().setBusinessName(seller.getBusinessDetails().getBusinessName());
        }
        if (seller.getBankDetails() != null
                && seller.getBankDetails().getAccountHolderName() != null
                && seller.getBankDetails().getIfscCode() != null
                && seller.getBankDetails().getAccountNumber() != null
        ){
            exitingSeller.getBankDetails().setAccountHolderName(
                    seller.getBankDetails().getAccountHolderName()
            );
            exitingSeller.getBankDetails().setAccountHolderName(
                    seller.getBankDetails().getIfscCode()
            );
            exitingSeller.getBankDetails().setAccountHolderName(
                    seller.getBankDetails().getAccountNumber()
            );
        }
        if (seller.getPickupAddress() != null
                && seller.getPickupAddress().getAddress() != null
                && seller.getPickupAddress().getMobile() != null
                && seller.getPickupAddress().getCity() != null
                && seller.getPickupAddress().getState() != null
        ){
            exitingSeller.getPickupAddress().setAddress(seller.getPickupAddress().getAddress());
            exitingSeller.getPickupAddress().setCity(seller.getPickupAddress().getCity());
            exitingSeller.getPickupAddress().setState(seller.getPickupAddress().getState());
            exitingSeller.getPickupAddress().setMobile(seller.getPickupAddress().getMobile());
            exitingSeller.getPickupAddress().setPinCode(seller.getPickupAddress().getPinCode());
        }if (seller.getGSTIN() != null){
            exitingSeller.setGSTIN(seller.getGSTIN());
        }

        return sellerRepository.save(exitingSeller);
    }

    @Override
    public void deleteSeller(Long id) throws Exception {

        Seller seller = getSellerById(id);
        sellerRepository.delete(seller);
    }

    @Override
    public Seller verifyEmail(String email, String otp) throws Exception {
        Seller seller = getSellerByEmail(email);
        seller.setEmailVerified(true);
        return sellerRepository.save(seller);
    }

    @Override
    public Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception {
        Seller seller = getSellerById(sellerId);
        seller.setAccountStatus(status);
        return sellerRepository.save(seller);
    }
}
